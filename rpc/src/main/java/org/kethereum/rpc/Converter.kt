package org.kethereum.rpc

import org.kethereum.model.Address
import org.kethereum.model.createTransactionWithDefaults
import org.kethereum.rpc.model.TransactionRPC
import org.walleth.khex.hexToByteArray
import java.math.BigInteger

fun TransactionRPC.toKethereumTransaction() = createTransactionWithDefaults(
        value = value.hexToBigInteger(),
        from = Address(from),
        to = to?.let { Address(it) },

        nonce = nonce.hexToBigInteger(),
        gasPrice = gasPrice.hexToBigInteger(),
        gasLimit = gas.hexToBigInteger(),
        txHash = hash,
        input = input.hexToByteArray().toList()
)

private fun String.hexToBigInteger() = BigInteger(replace("0x", ""), 16)