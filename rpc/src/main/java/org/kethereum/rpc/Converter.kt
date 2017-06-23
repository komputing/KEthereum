package org.kethereum.rpc

import org.kethereum.functions.hexToByteArray
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.TransactionRPC
import java.math.BigInteger

fun TransactionRPC.toKethereumTransaction() = Transaction(
        value = value.hexToBigInteger(),
        from = Address(from),
        to = to?.let { Address(it) },

        nonce = nonce.hexToBigInteger().toLong(),
        gasPrice = gasPrice.hexToBigInteger(),
        gasLimit = gas.hexToBigInteger(),
        txHash = hash,
        input = input.hexToByteArray().toList()
)

private fun String.hexToBigInteger() = BigInteger(replace("0x", ""), 16)