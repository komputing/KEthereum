package org.kethereum.model

import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.DEFAULT_GAS_PRICE
import java.math.BigInteger

data class Transaction(
        var chain: ChainDefinition?,
        var creationEpochSecond: Long?,
        var from: Address?,
        var gasLimit: BigInteger,
        var gasPrice: BigInteger,
        var input: List<Byte>,
        var nonce: BigInteger?,
        var to: Address?,
        var txHash: String?,
        var value: BigInteger
) {
    constructor() : this(
            chain = null,
            creationEpochSecond = null,
            from = null,
            gasLimit = DEFAULT_GAS_LIMIT,
            gasPrice = DEFAULT_GAS_PRICE,
            input = emptyList<Byte>(),
            nonce = null,
            to = null,
            txHash = null,
            value = BigInteger.ZERO
    )
}

// we cannot use default values in the data class when we want to use it with room
fun createTransactionWithDefaults(
        chain: ChainDefinition? = null,
        creationEpochSecond: Long? = null,
        from: Address,
        gasLimit: BigInteger = DEFAULT_GAS_LIMIT,
        gasPrice: BigInteger = DEFAULT_GAS_PRICE,
        input: List<Byte> = emptyList(),
        nonce: BigInteger? = null,
        to: Address?,
        txHash: String? = null,
        value: BigInteger
) = Transaction(chain, creationEpochSecond, from, gasLimit, gasPrice, input, nonce, to, txHash, value)

