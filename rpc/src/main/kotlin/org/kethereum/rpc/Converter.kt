package org.kethereum.rpc

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toHexString
import org.kethereum.model.*
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.rpc.BlockInformationRPC
import org.kethereum.rpc.model.rpc.TransactionRPC
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString

internal fun TransactionRPC.toKethereumTransaction() = SignedTransaction(
        createTransactionWithDefaults(
                value = value.hexToBigInteger(),
                from = Address(from),
                to = to?.let { Address(it) },
                chain = chainId?.hexToBigInteger()?.let { ChainId(it.toLong()) },

                nonce = nonce.hexToBigInteger(),
                gasPrice = gasPrice.hexToBigInteger(),
                gasLimit = gas.hexToBigInteger(),
                txHash = hash,
                input = input.hexToByteArray()

        ), signatureData = SignatureData(r = r.hexToBigInteger(), s = s.hexToBigInteger(), v = v.hexToBigInteger().toByte())
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