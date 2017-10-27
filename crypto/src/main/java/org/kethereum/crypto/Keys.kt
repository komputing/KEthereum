package org.kethereum.crypto

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.kethereum.crypto.SecureRandomUtils.secureRandom
import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.keccakshortcut.keccak
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
        Security.addProvider(BouncyCastleProvider())
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

        val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC")
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

}
