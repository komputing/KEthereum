package org.kethereum.crypto

import org.kethereum.crypto.SecureRandomUtils.secureRandom
import org.kethereum.extensions.toBytesPadded
import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.keccakshortcut.keccak
import org.spongycastle.math.ec.ECPoint
import org.walleth.khex.clean0xPrefix
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString
import java.math.BigInteger
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.util.*




/**
 * Crypto key utilities.
 */
object Keys {

    const val PRIVATE_KEY_SIZE = 32
    const val PUBLIC_KEY_SIZE = 64

    const val ADDRESS_LENGTH_IN_HEX = 40
    val PUBLIC_KEY_LENGTH_IN_HEX = PUBLIC_KEY_SIZE shl 1

    init {
        Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
    }

    /**
     * Create a keypair using SECP-256k1 curve.
     *
     *
     * Private keypairs are encoded using PKCS8
     *
     *
     * Private keys are encoded using X.509
     */
    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class)
    internal fun createSecp256k1KeyPair(): KeyPair {

        val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA")
        val ecGenParameterSpec = ECGenParameterSpec("secp256k1")
        keyPairGenerator.initialize(ecGenParameterSpec, secureRandom())
        return keyPairGenerator.generateKeyPair()
    }

    @Throws(InvalidAlgorithmParameterException::class, NoSuchAlgorithmException::class, NoSuchProviderException::class)
    fun createEcKeyPair(): ECKeyPair {
        val keyPair = createSecp256k1KeyPair()
        return ECKeyPair.create(keyPair)
    }

    fun getAddress(ecKeyPair: ECKeyPair) = getAddress(ecKeyPair.publicKey)


    fun getAddress(publicKey: BigInteger) = getAddress(publicKey.toHexStringZeroPadded(PUBLIC_KEY_LENGTH_IN_HEX))


    fun getAddress(publicKey: String): String {
        var publicKeyNoPrefix = publicKey.clean0xPrefix()

        if (publicKeyNoPrefix.length < PUBLIC_KEY_LENGTH_IN_HEX) {
            publicKeyNoPrefix = "0".repeat(PUBLIC_KEY_LENGTH_IN_HEX - publicKeyNoPrefix.length) + publicKeyNoPrefix
        }
        val hexToByteArray = publicKeyNoPrefix.hexToByteArray()
        val hash = hexToByteArray.keccak().toHexString()

        return hash.substring(hash.length - ADDRESS_LENGTH_IN_HEX)  // right most 160 bits
    }

    fun getAddress(publicKey: ByteArray): ByteArray {
        val hash = publicKey.keccak()
        return Arrays.copyOfRange(hash, hash.size - 20, hash.size)  // right most 160 bits
    }

    fun getCompressedPublicKey(keypair: ECKeyPair) : ByteArray {
        //add the uncompressed prefix
        val ret = keypair.publicKey.toBytesPadded(PUBLIC_KEY_SIZE + 1)
        ret[0] = 4
        val point = CURVE.curve.decodePoint(ret)
        return point.getEncoded(true)
    }

    /**
     * Decodes an uncompressed public key (without 0x04 prefix) given an ECPoint
     */
    fun publicKeyFromPoint(point: ECPoint): BigInteger {
        val encoded = point.getEncoded(false)
        return BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.size))
    }

}
