package org.kethereum.extensions.transactions

import org.kethereum.extensions.startsWith
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.extensions.toNoPrefixHexString
import java.math.BigInteger

fun Transaction.calculateHash() = encodeLegacyTxRLP().keccak()

val tokenTransferSignature = listOf(0xa9.toByte(), 0x05.toByte(), 0x9c.toByte(), 0xbb.toByte())

fun Transaction.isTokenTransfer() = input.toList().startsWith(tokenTransferSignature)
fun Transaction.getTokenTransferValue() = BigInteger(input.toList().subList(input.size - 32, input.size).toNoPrefixHexString(), 16)
fun Transaction.getTokenTransferTo() = Address(input.toList().subList(input.size - 32 - 20, input.size - 32).toHexString())

val tokenMintSignature = listOf(0x40.toByte(), 0xc1.toByte(), 0x0f.toByte(), 0x19.toByte())

fun Transaction.isTokenMint() = input.toList().startsWith(tokenMintSignature)
fun Transaction.getTokenMintValue() = getTokenTransferValue() // same parameters
fun Transaction.getTokenMintTo() = getTokenTransferTo() // same parameters

fun Transaction.getTokenRelevantTo() = when {
    isTokenTransfer() -> getTokenTransferTo()
    isTokenMint() -> getTokenMintTo()
    else -> null
}

fun Transaction.getTokenRelevantValue() = when {
    isTokenTransfer() -> getTokenTransferValue()
    isTokenMint() -> getTokenMintValue()
    else -> null
}