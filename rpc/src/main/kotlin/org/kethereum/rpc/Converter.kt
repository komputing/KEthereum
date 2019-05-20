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

        var res = """{"to":"$to""""

        if (from != null) {
                res += ""","from":"$from""""
        }

        if (input.isNotEmpty()) {
                res += ""","data":"${input.toHexString()}""""
        }

        if (gasLimit != null) {
                res += ""","gas":"${gasLimit!!.toHexString()}""""
        }

        if (gasPrice != null) {
                res += ""","gasPrice":"${gasPrice!!.toHexString()}""""
        }

        if (value != null) {
                res += ""","value":"${value!!.toHexString()}""""
        }

        return "$res}"
}