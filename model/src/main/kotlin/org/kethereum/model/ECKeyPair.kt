package org.kethereum.model

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toBigInteger
import org.komputing.khex.model.HexString
import java.math.BigInteger

@JvmInline
value class PrivateKey(val key: BigInteger) {
    constructor(privateKey: ByteArray) : this(privateKey.toBigInteger())
    constructor(hex: HexString) : this(hex.hexToBigInteger())
}

@JvmInline
value class PublicKey(val key: BigInteger) {

    constructor(publicKey: ByteArray) : this(publicKey.toBigInteger())
    constructor(publicKey: HexString) : this(publicKey.hexToBigInteger())

    override fun toString() = key.toString()
}

data class ECKeyPair(val privateKey: PrivateKey, val publicKey: PublicKey)
