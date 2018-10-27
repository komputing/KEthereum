package org.kethereum.crypto.model

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toBigInteger
import java.math.BigInteger

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

    constructor(publicKey: ByteArray) : this(publicKey.toBigInteger())
    constructor(publicKey: String) : this(publicKey.hexToBigInteger())

    override fun equals(other: Any?) = when (other) {
        is PublicKey -> other.key == key
        else -> false
    }

    override fun hashCode() = key.hashCode()
    override fun toString() = key.toString()
}

data class ECKeyPair(val privateKey: PrivateKey, val publicKey: PublicKey)
