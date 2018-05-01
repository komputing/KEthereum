package org.kethereum.bip32

import org.kethereum.bip44.BIP44.Companion.isHardened
import org.kethereum.crypto.*
import org.kethereum.encodings.decodeBase58WithChecksum
import org.kethereum.encodings.encodeToBase58WithChecksum
import org.kethereum.extensions.toBytesPadded
import org.kethereum.hashes.ripemd160
import org.kethereum.hashes.sha256
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.InvalidKeyException
import java.security.KeyException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

data class ExtendedKey(val keyPair: ECKeyPair,
                       private val chainCode: ByteArray,
                       private val depth: Byte,
                       private val parentFingerprint: Int,
                       private val sequence: Int) {

    fun generateChildKey(element: Int): ExtendedKey {
        try {
            if (isHardened(element) && keyPair.privateKey == BigInteger.ZERO) {
                throw IllegalArgumentException("need private key for private generation using hardened paths")
            }
            val mac = Mac.getInstance("HmacSHA512")
            val key = SecretKeySpec(chainCode, "HmacSHA512")
            mac.init(key)

            val extended: ByteArray
            val pub = keyPair.getCompressedPublicKey()
            if (isHardened(element)) {
                val privateKeyPaddedBytes = keyPair.privateKey.toBytesPadded(PRIVATE_KEY_SIZE)

                extended = ByteBuffer
                        .allocate(privateKeyPaddedBytes.size + 5)
                        .order(ByteOrder.BIG_ENDIAN)
                        .put(0)
                        .put(privateKeyPaddedBytes)
                        .putInt(element)
                        .array()
            } else {
                //non-hardened
                extended = ByteBuffer
                        .allocate(pub.size + 4)
                        .order(ByteOrder.BIG_ENDIAN)
                        .put(pub)
                        .putInt(element)
                        .array()
            }
            val lr = mac.doFinal(extended)
            val l = Arrays.copyOfRange(lr, 0, PRIVATE_KEY_SIZE)
            val r = Arrays.copyOfRange(lr, PRIVATE_KEY_SIZE, PRIVATE_KEY_SIZE + CHAINCODE_SIZE)

            val m = BigInteger(1, l)
            if (m >= CURVE.n) {
                throw KeyException("Child key derivation resulted in a key with higher modulus. Suggest deriving the next increment.")
            }

            return if (keyPair.privateKey != BigInteger.ZERO) {
                val k = m.add(keyPair.privateKey).mod(CURVE.n)
                if (k == BigInteger.ZERO) {
                    throw KeyException("Child key derivation resulted in zeros. Suggest deriving the next increment.")
                }
                ExtendedKey(ECKeyPair.create(k), r, (depth + 1).toByte(), computeFingerPrint(keyPair), element)
            } else {
                val q = CURVE.g.multiply(m).add(CURVE.curve.decodePoint(pub)).normalize()
                if (q.isInfinity) {
                    throw KeyException("Child key derivation resulted in zeros. Suggest deriving the next increment.")
                }
                val point = CURVE.curve.createPoint(q.xCoord.toBigInteger(), q.yCoord.toBigInteger())

                ExtendedKey(ECKeyPair(BigInteger.ZERO, point.toPublicKey()), r, (depth + 1).toByte(), computeFingerPrint(keyPair), element)
            }
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException(e)
        } catch (e: NoSuchProviderException) {
            throw KeyException(e)
        } catch (e: InvalidKeyException) {
            throw KeyException(e)
        }

    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExtendedKey

        if (keyPair != other.keyPair) return false
        if (!Arrays.equals(chainCode, other.chainCode)) return false
        if (depth != other.depth) return false
        if (parentFingerprint != other.parentFingerprint) return false
        if (sequence != other.sequence) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyPair.hashCode()
        result = 31 * result + Arrays.hashCode(chainCode)
        result = 31 * result + depth
        result = 31 * result + parentFingerprint
        result = 31 * result + sequence
        return result
    }

    fun serialize(publicKeyOnly: Boolean = false): String {
        val out = ByteBuffer.allocate(Companion.EXTENDED_KEY_SIZE)
        try {
            if (publicKeyOnly || keyPair.privateKey == BigInteger.ZERO) {
                out.put(xpub)
            } else {
                out.put(xprv)
            }
            out.put(depth)
            out.putInt(parentFingerprint)
            out.putInt(sequence)
            out.put(chainCode)
            if (publicKeyOnly || keyPair.privateKey == BigInteger.ZERO) {
                out.put(keyPair.getCompressedPublicKey())
            } else {
                out.put(0x00)
                out.put(keyPair.privateKey.toBytesPadded(PRIVATE_KEY_SIZE))
            }
        } catch (e: IOException) {
        }

        return out.array().encodeToBase58WithChecksum()
    }

    companion object {

        private val BITCOIN_SEED = "Bitcoin seed".toByteArray()
        private const val CHAINCODE_SIZE = PRIVATE_KEY_SIZE
        private const val COMPRESSED_PUBLIC_KEY_SIZE = PRIVATE_KEY_SIZE + 1
        private const val EXTENDED_KEY_SIZE: Int = 78
        internal val xprv = byteArrayOf(0x04, 0x88.toByte(), 0xAD.toByte(), 0xE4.toByte())
        internal val xpub = byteArrayOf(0x04, 0x88.toByte(), 0xB2.toByte(), 0x1E.toByte())

        fun createFromSeed(seed: ByteArray, publicKeyOnly: Boolean = false): ExtendedKey {
            try {
                val mac = Mac.getInstance("HmacSHA512")
                val seedKey = SecretKeySpec(BITCOIN_SEED, "HmacSHA512")
                mac.init(seedKey)
                val lr = mac.doFinal(seed)
                val l = Arrays.copyOfRange(lr, 0, PRIVATE_KEY_SIZE)
                val r = Arrays.copyOfRange(lr, PRIVATE_KEY_SIZE, PRIVATE_KEY_SIZE + CHAINCODE_SIZE)
                val m = BigInteger(1, l)
                if (m >= CURVE.n) {
                    throw KeyException("Master key creation resulted in a key with higher modulus. Suggest deriving the next increment.")
                }
                val keyPair = ECKeyPair.create(l)
                return if (publicKeyOnly) {
                    val pubKeyPair = ECKeyPair(BigInteger.ZERO, keyPair.publicKey)
                    ExtendedKey(pubKeyPair, r, 0, 0, 0)
                } else {
                    ExtendedKey(keyPair, r, 0, 0, 0)
                }
            } catch (e: NoSuchAlgorithmException) {
                throw KeyException(e)
            } catch (e: NoSuchProviderException) {
                throw KeyException(e)
            } catch (e: InvalidKeyException) {
                throw KeyException(e)
            }

        }

        /**
         * Gets an [Int] representation of public key hash
         * @return an Int built from the first 4 bytes of the result of hash160 over the compressed public key
         */
        private fun computeFingerPrint(keyPair: ECKeyPair): Int {
            val pubKeyHash = keyPair.getCompressedPublicKey()
                    .sha256()
                    .ripemd160()
            var fingerprint = 0
            for (i in 0..3) {
                fingerprint = fingerprint shl 8
                fingerprint = fingerprint or (pubKeyHash[i].toInt() and 0xff)
            }
            return fingerprint
        }

        fun parse(serialized: String): ExtendedKey {
            val data = serialized.decodeBase58WithChecksum()
            if (data.size != EXTENDED_KEY_SIZE) {
                throw KeyException("invalid extended key")
            }

            val buff = ByteBuffer
                    .wrap(data)
                    .order(ByteOrder.BIG_ENDIAN)

            val type = ByteArray(4)

            buff.get(type)

            val hasPrivate  = when {
                Arrays.equals(type, xprv) -> true
                Arrays.equals(type, xpub) -> false
                else -> throw KeyException("invalid magic number for an extended key")
            }

            val depth = buff.get()
            val parent = buff.int
            val sequence = buff.int

            val chainCode = ByteArray(PRIVATE_KEY_SIZE)
            buff.get(chainCode)

            val keyPair = if (hasPrivate) {
                buff.get() // ignore the leading 0
                val privateBytes = ByteArray(PRIVATE_KEY_SIZE)
                buff.get(privateBytes)
                ECKeyPair.create(privateBytes)
            } else {
                val compressedPublicBytes = ByteArray(COMPRESSED_PUBLIC_KEY_SIZE)
                buff.get(compressedPublicBytes)
                val uncompressedPublicBytes = decompressKey(compressedPublicBytes)
                ECKeyPair(BigInteger.ZERO, BigInteger(1, uncompressedPublicBytes))
            }
            return ExtendedKey(keyPair, chainCode, depth, parent, sequence)
        }

    }
}
