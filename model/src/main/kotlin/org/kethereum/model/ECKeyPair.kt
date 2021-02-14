package org.kethereum.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toBigInteger
import org.komputing.khex.model.HexString

inline class PrivateKey(val key: BigInteger) {
    constructor(privateKey: ByteArray) : this(privateKey.toBigInteger())
    constructor(hex: HexString) : this(hex.hexToBigInteger())
}

inline class PublicKey(val key: BigInteger) {

    constructor(publicKey: ByteArray) : this(publicKey.toBigInteger())
    constructor(publicKey: HexString) : this(publicKey.hexToBigInteger())

    override fun toString() = key.toString()
}

data class ECKeyPair(val privateKey: PrivateKey, val publicKey: PublicKey)
