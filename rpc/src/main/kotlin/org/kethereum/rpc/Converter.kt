package org.kethereum.rpc

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toHexString
import org.kethereum.model.*
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.rpc.BlockInformationRPC
import org.kethereum.rpc.model.rpc.TransactionRPC
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
                gasPrice = HexString(gasPrice).hexToBigInteger(),
                gasLimit = HexString(gas).hexToBigInteger(),
                txHash = hash,
                input = HexString(input).hexToByteArray()

        ), signatureData = SignatureData(
        r = HexString(r).hexToBigInteger(),
        s = HexString(s).hexToBigInteger(),
        v = HexString(v).hexToBigInteger())
)

internal fun BlockInformationRPC.toBlockInformation() = BlockInformation(transactions.map { it.toKethereumTransaction() })

internal fun Transaction.toJSON(): String {

    val elements = mutableListOf<String>()

    if (from != null) {
        elements.add(""""from":"$from"""")
    }

    elements.add(""""to":"$to"""")

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