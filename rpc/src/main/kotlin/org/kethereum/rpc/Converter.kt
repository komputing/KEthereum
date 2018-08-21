package org.kethereum.rpc

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.SignedTransaction
import org.kethereum.model.createTransactionWithDefaults
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.rpc.BlockInformationRPC
import org.kethereum.rpc.model.rpc.TransactionRPC
import org.walleth.khex.hexToByteArray

internal fun TransactionRPC.toKethereumTransaction() = SignedTransaction(
        createTransactionWithDefaults(
                value = value.hexToBigInteger(),
                from = Address(from),
                to = to?.let { Address(it) },

                nonce = nonce.hexToBigInteger(),
                gasPrice = gasPrice.hexToBigInteger(),
                gasLimit = gas.hexToBigInteger(),
                txHash = hash,
                input = input.hexToByteArray().toList()

        ), signatureData = SignatureData(r = r.hexToBigInteger(), s = s.hexToBigInteger(), v = v.hexToBigInteger().byteValueExact())
)

internal fun BlockInformationRPC.toBlockInformation() = BlockInformation(transactions.map { it.toKethereumTransaction() })