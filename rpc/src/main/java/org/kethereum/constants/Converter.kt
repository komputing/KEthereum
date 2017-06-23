package org.kethereum.constants

import org.kethereum.functions.hexToByteArray
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.constants.model.TransactionRPC
import java.math.BigInteger

fun TransactionRPC.toKethereumTransaction() = Transaction(
        value = BigInteger(value),
        from = Address(from),
        to = to?.let { Address(it) },

        nonce = nonce.toLongOrNull(),
        gasPrice = BigInteger(gasPrice),
        gasLimit = BigInteger(gas),
        txHash = hash,
        input = input.hexToByteArray().toList()
)