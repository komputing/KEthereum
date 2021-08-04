package org.kethereum.model

import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.DEFAULT_GAS_PRICE
import java.math.BigInteger

data class Transaction(
    var chain: BigInteger?,
    var creationEpochSecond: Long?,
    var from: Address?,
    var gasLimit: BigInteger?,
    var gasPrice: BigInteger?,
    var input: ByteArray,
    var nonce: BigInteger?,
    var to: Address?,
    var txHash: String?,
    var value: BigInteger?,
    var blockHash: String?,
    var blockNumber: BigInteger?,
    var maxPriorityFeePerGas: BigInteger?,
    var maxFeePerGas: BigInteger?
) {
    constructor() : this(
        chain = null,
        creationEpochSecond = null,
        from = null,
        gasLimit = DEFAULT_GAS_LIMIT,
        gasPrice = DEFAULT_GAS_PRICE,
        input = ByteArray(0),
        nonce = null,
        to = null,
        txHash = null,
        blockHash = null,
        blockNumber = null,
        value = BigInteger.ZERO,
        maxPriorityFeePerGas = null,
        maxFeePerGas = null
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (chain != other.chain) return false
        if (creationEpochSecond != other.creationEpochSecond) return false
        if (from != other.from) return false
        if (gasLimit != other.gasLimit) return false
        if (gasPrice != other.gasPrice) return false
        if (!input.contentEquals(other.input)) return false
        if (nonce != other.nonce) return false
        if (to != other.to) return false
        if (txHash != other.txHash) return false
        if (value != other.value) return false
        if (maxPriorityFeePerGas != other.maxPriorityFeePerGas) return false
        if (maxFeePerGas != other.maxFeePerGas) return false
        return true
    }

    override fun hashCode(): Int {
        var result = chain?.hashCode() ?: 0
        result = 31 * result + (creationEpochSecond?.hashCode() ?: 0)
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (gasLimit?.hashCode() ?: 0)
        result = 31 * result + (gasPrice?.hashCode() ?: 0)
        result = 31 * result + input.contentHashCode()
        result = 31 * result + (nonce?.hashCode() ?: 0)
        result = 31 * result + (to?.hashCode() ?: 0)
        result = 31 * result + (txHash?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (maxPriorityFeePerGas?.hashCode() ?: 0)
        result = 31 * result + (maxFeePerGas?.hashCode() ?: 0)
        return result
    }
}

// we cannot use default values in the data class when we want to use it with room
fun createTransactionWithDefaults(
    chain: ChainId? = null,
    creationEpochSecond: Long? = null,
    from: Address,
    gasLimit: BigInteger = DEFAULT_GAS_LIMIT,
    gasPrice: BigInteger? = DEFAULT_GAS_PRICE,
    input: ByteArray = ByteArray(0),
    nonce: BigInteger? = null,
    to: Address?,
    txHash: String? = null,
    value: BigInteger,
    blockHash: String? = null,
    blockNumber: BigInteger? = null,
    maxPriorityFeePerGas: BigInteger? = null,
    maxFeePerGas: BigInteger? = null,

) = Transaction(chain?.value, creationEpochSecond, from, gasLimit, gasPrice, input, nonce, to, txHash, value, blockHash, blockNumber, maxPriorityFeePerGas, maxFeePerGas)


fun createEmptyTransaction() = Transaction(null, null, null, null, null, ByteArray(0), null, null, null, null, null, null, null, null)
