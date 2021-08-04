package org.kethereum.rpc

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toHexString
import org.kethereum.model.*
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.rpc.BlockInformationRPC
import org.kethereum.rpc.model.rpc.TransactionRPC
import org.komputing.khex.extensions.clean0xPrefix
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString

internal fun TransactionRPC.toKethereumTransaction() = SignedTransaction(
    createTransactionWithDefaults(
        value = HexString(value).hexToBigInteger(),
        from = Address(from),
        to = to?.let { Address(it) },
        chain = chainId?.let { HexString(it).hexToBigInteger() }?.let { ChainId(it) },

        nonce = HexString(nonce).hexToBigInteger(),
        gasPrice = gasPrice?.let { HexString(it).hexToBigInteger() },
        gasLimit = HexString(gas).hexToBigInteger(),
        txHash = hash,
        input = HexString(input).hexToByteArray(),
        blockHash = blockHash,
        blockNumber = blockNumber?.let { HexString(blockNumber).hexToBigInteger() },
        maxFeePerGas = maxFeePerGas?.let { HexString(maxFeePerGas).hexToBigInteger() },
        maxPriorityFeePerGas = maxPriorityFeePerGas?.let { HexString(maxPriorityFeePerGas).hexToBigInteger() }
    ), signatureData = SignatureData(
        r = HexString(r).hexToBigInteger(),
        s = HexString(s).hexToBigInteger(),
        v = HexString(v).hexToBigInteger()
    )
)

internal fun BlockInformationRPC.toBlockInformation() = BlockInformation(
    number = HexString(number).hexToBigInteger(),
    hash = HexString(hash),
    parentHash = HexString(parentHash),
    nonce = HexString(nonce).hexToBigInteger(),
    sha3Uncles = HexString(sha3Uncles),
    logsBloom = HexString(logsBloom),
    transactionsRoot = HexString(transactionsRoot),
    stateRoot = HexString(stateRoot),
    miner = HexString(miner),
    difficulty = HexString(difficulty).hexToBigInteger(),
    totalDifficulty = HexString(totalDifficulty).hexToBigInteger(),
    extraData = HexString(extraData),
    size = HexString(size).hexToBigInteger(),
    gasLimit = HexString(gasLimit).hexToBigInteger(),
    gasUsed = HexString(gasUsed).hexToBigInteger(),
    timestamp = HexString(timestamp).hexToBigInteger(),
    uncles = uncles.map { HexString(it) },
    transactions = transactions.map { it.toKethereumTransaction() },
    baseFeePerGas = baseFeePerGas?.let { HexString(it).hexToBigInteger() }
)

internal fun Transaction.toJSON(): String {

    val elements = mutableListOf<String>()

    if (from != null) {
        elements.add(""""from":"$from"""")
    }

    if (to != null) {
        elements.add(""""to":"$to"""")
    }

    if (input.isNotEmpty()) {
        elements.add(""""data":"${input.toHexString()}"""")
    }

    if (gasLimit != null) {
        elements.add(""""gas":"${gasLimit!!.toHexString()}"""")
    }

    if (gasPrice != null) {
        elements.add(""""gasPrice":"${gasPrice!!.toHexString()}"""")
    }

    if (value != null) {
        elements.add(""""value":"${value!!.toHexString()}"""")
    }

    return "{${elements.joinToString(",")}}"
}