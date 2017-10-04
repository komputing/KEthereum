package org.kethereum.model

import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.DEFAULT_GAS_PRICE
import java.math.BigInteger

data class Transaction(val value: BigInteger,
                       val from: Address,
                       val to: Address?,
                       val creationEpochSecond: Long?,
                       var nonce: BigInteger?,
                       var gasPrice: BigInteger,
                       var gasLimit: BigInteger,
                       var txHash: String?,
                       var input: List<Byte>)

// we cannot use default values in the data class when we want to use it with room
fun createTransactionWithDefaults(
        value: BigInteger,
        from: Address,
        to: Address?,
        creationEpochSecond: Long? = null,
        nonce: BigInteger? = null,
        gasPrice: BigInteger = DEFAULT_GAS_PRICE,
        gasLimit: BigInteger = DEFAULT_GAS_LIMIT,
        txHash: String? = null,
        input: List<Byte> = emptyList()
) = Transaction(value, from, to, creationEpochSecond, nonce, gasPrice, gasLimit, txHash, input)
