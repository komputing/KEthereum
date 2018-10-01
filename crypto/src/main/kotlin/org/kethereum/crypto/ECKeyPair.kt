package org.kethereum.crypto

import org.kethereum.extensions.toBigInteger
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import java.math.BigInteger
import java.security.KeyPair
import java.util.*

class PrivateKey(val key: BigInteger) {
    constructor(privateKey: ByteArray) : this(privateKey.toBigInteger())

    override fun equals(other: Any?) = when (other) {
        is PrivateKey -> other.key == key
        else -> false
    }

    override fun hashCode() = key.hashCode()
    override fun toString() = key.toString()
}

class PublicKey(val key: BigInteger) {

    override fun equals(other: Any?) = when (other) {
        is PublicKey -> other.key == key
        else -> false
    }

    override fun hashCode() = key.hashCode()
    override fun toString() = key.toString()
}

data class ECKeyPair(val privateKey: PrivateKey, val publicKey: PublicKey)

fun PrivateKey.toECKeyPair() = ECKeyPair(this, publicKeyFromPrivate(this))


fun KeyPair.toECKeyPair(): ECKeyPair {
    val privateKey = private as BCECPrivateKey
    val publicKey = public as BCECPublicKey

    val privateKeyValue = privateKey.d

    // Ethereum does not use encoded public keys like bitcoin - see
    // https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
    // Additionally, as the first bit is a constant prefix (0x04) we ignore this value
    val publicKeyBytes = publicKey.q.getEncoded(false)
    val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))

    return ECKeyPair(PrivateKey(privateKeyValue), PublicKey(publicKeyValue))
}