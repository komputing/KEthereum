package org.kethereum.crypto

import org.kethereum.extensions.toBigInteger
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import java.math.BigInteger
import java.security.KeyPair
import java.util.*

/**
 * Elliptic Curve SECP-256k1 generated key pair.
 */
data class ECKeyPair(val privateKey: BigInteger, val publicKey: BigInteger) {

    companion object {

        fun create(keyPair: KeyPair): ECKeyPair {
            val privateKey = keyPair.private as BCECPrivateKey
            val publicKey = keyPair.public as BCECPublicKey

            val privateKeyValue = privateKey.d

            // Ethereum does not use encoded public keys like bitcoin - see
            // https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
            // Additionally, as the first bit is a constant prefix (0x04) we ignore this value
            val publicKeyBytes = publicKey.q.getEncoded(false)
            val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))

            return ECKeyPair(privateKeyValue, publicKeyValue)
        }

        fun create(privateKey: BigInteger) = ECKeyPair(privateKey, publicKeyFromPrivate(privateKey))

        fun create(privateKey: ByteArray) = create(privateKey.toBigInteger())
    }
}
