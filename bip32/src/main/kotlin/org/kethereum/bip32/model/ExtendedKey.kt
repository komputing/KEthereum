package org.kethereum.bip32.model

import org.kethereum.crypto.getCompressedPublicKey
import org.kethereum.extensions.toBytesPadded
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PRIVATE_KEY_SIZE
import org.komputing.kbase58.encodeToBase58WithChecksum
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.KeyException


data class ExtendedKey(val keyPair: ECKeyPair,
                       internal val chainCode: ByteArray,
                       internal val depth: Byte,
                       private val parentFingerprint: Int,
                       private val sequence: Int,
                       internal val versionBytes: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExtendedKey

        if (keyPair != other.keyPair) return false
        if (!versionBytes.contentEquals(other.versionBytes)) return false
        if (!chainCode.contentEquals(other.chainCode)) return false
        if (depth != other.depth) return false
        if (parentFingerprint != other.parentFingerprint) return false
        if (sequence != other.sequence) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyPair.hashCode()
        result = 31 * result + versionBytes.contentHashCode()
        result = 31 * result + chainCode.contentHashCode()
        result = 31 * result + depth
        result = 31 * result + parentFingerprint
        result = 31 * result + sequence
        return result
    }

    fun serialize(publicKeyOnly: Boolean = false): String {
        val out = ByteBuffer.allocate(EXTENDED_KEY_SIZE)
        try {

            if (!publicKeyOnly && !(versionBytes contentEquals xprv) && !(versionBytes contentEquals tprv))
                throw KeyException("The extended version bytes dedicated to public keys. Suggest using publicKeyOnly mode")

            if (!publicKeyOnly && keyPair.privateKey.key == BigInteger.ZERO)
                throw KeyException("The extended key doesn't provide any private key. Suggest using publicKeyOnly mode")

            out.put(if (publicKeyOnly && versionBytes contentEquals xprv) xpub else if (publicKeyOnly && versionBytes contentEquals tprv) tpub else versionBytes)
            out.put(depth)
            out.putInt(parentFingerprint)
            out.putInt(sequence)
            out.put(chainCode)
            if (publicKeyOnly) {
                out.put(keyPair.getCompressedPublicKey())
            } else {
                out.put(0x00)
                out.put(keyPair.privateKey.key.toBytesPadded(PRIVATE_KEY_SIZE))
            }
        } catch (e: IOException) {
        }

        return out.array().encodeToBase58WithChecksum()
    }

}
