package org.kethereum.functions

import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString
import java.math.BigInteger


fun Transaction.isTokenTransfer() = input.startsWith(tokenTransferSignature)
fun Transaction.getTokenTransferValue() = BigInteger(input.subList(input.size - 32, input.size).toHexString(""), 16)
fun Transaction.getTokenTransferTo() = Address(input.subList(input.size - 32 - 20, input.size - 32).toHexString())

val tokenTransferSignature = listOf(0xa9.toByte(), 0x05.toByte(), 0x9c.toByte(), 0xbb.toByte())

fun createTokenTransferTransactionInput(address: Address, currentAmount: BigInteger?): List<Byte>
        = (tokenTransferSignature.toHexString() + "000000000000000000000000" + address.hex.replace("0x", "")
        + String.format("%064x", currentAmount)).hexToByteArray().toList()

fun Transaction.calculateHash() = encodeRLP().keccak()