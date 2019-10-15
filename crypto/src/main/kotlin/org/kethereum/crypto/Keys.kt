package org.kethereum.crypto

import org.kethereum.extensions.toBytesPadded
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PUBLIC_KEY_SIZE

/**
 * Create a keypair using SECP-256k1 curve.
 *
 *
 * Private keypairs are encoded using PKCS8
 *
 *
 * Private keys are encoded using X.509
 */
fun createEthereumKeyPair(): ECKeyPair = CryptoAPI.keyPairGenerator.generate()

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
    return encoded.copyOfRange(1, encoded.size)
}
