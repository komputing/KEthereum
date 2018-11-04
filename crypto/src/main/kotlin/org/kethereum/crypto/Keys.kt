package org.kethereum.crypto

import org.kethereum.crypto.SecureRandomUtils.secureRandom
import org.kethereum.crypto.model.ECKeyPair
import org.kethereum.crypto.model.PUBLIC_KEY_SIZE
import org.kethereum.crypto.model.PrivateKey
import org.kethereum.crypto.model.PublicKey
import org.kethereum.extensions.toBytesPadded
import org.spongycastle.crypto.generators.ECKeyPairGenerator
import org.spongycastle.crypto.params.ECKeyGenerationParameters
import org.spongycastle.crypto.params.ECPrivateKeyParameters
import org.spongycastle.crypto.params.ECPublicKeyParameters
import java.math.BigInteger
import java.util.*

/**
 * Create a keypair using SECP-256k1 curve.
 *
 *
 * Private keypairs are encoded using PKCS8
 *
 *
 * Private keys are encoded using X.509
 */
fun createEthereumKeyPair() =
    ECKeyPairGenerator().run {
        init(ECKeyGenerationParameters(CURVE_DOMAIN_PARAMS, secureRandom()))
        generateKeyPair().run {
            val privateKeyValue = (private as ECPrivateKeyParameters).d
            val publicKeyBytes = (public as ECPublicKeyParameters).q.getEncoded(false)
            val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))
            ECKeyPair(PrivateKey(privateKeyValue), PublicKey(publicKeyValue))
        }
    }

fun ECKeyPair.getCompressedPublicKey(): ByteArray {
    //add the uncompressed prefix
    val ret = publicKey.key.toBytesPadded(PUBLIC_KEY_SIZE + 1)
    ret[0] = 4
    val point = CURVE.decodePoint(ret)
    return point.encoded(true)
}

/**
 * Takes a public key in compressed encoding (including prefix)
 * and returns the key in uncompressed encoding (without prefix)
 */
fun decompressKey(publicBytes: ByteArray): ByteArray {
    val point = CURVE.decodePoint(publicBytes)
    val encoded = point.encoded()
    return Arrays.copyOfRange(encoded, 1, encoded.size)
}
