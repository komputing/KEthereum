package org.kethereum.functions

import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.model.extensions.format
import org.kethereum.model.extensions.hexToByteArray
import org.kethereum.model.extensions.toHexString
import org.kethereum.model.extensions.toNoPrefixHexString
import org.kethereum.model.number.BigInteger


fun Transaction.isTokenTransfer() = input.startsWith(tokenTransferSignature)
fun Transaction.getTokenTransferValue() = BigInteger(input.subList(input.size - 32, input.size).toNoPrefixHexString(), 16)
fun Transaction.getTokenTransferTo() = Address(input.subList(input.size - 32 - 20, input.size - 32).toHexString())

val tokenTransferSignature = listOf(0xa9.toByte(), 0x05.toByte(), 0x9c.toByte(), 0xbb.toByte())

fun createTokenTransferTransactionInput(address: Address, currentAmount: BigInteger): List<Byte>
        = (tokenTransferSignature.toHexString() + "0".repeat(24) + address.cleanHex
        + "%064x".format(currentAmount)).hexToByteArray().toList()

fun Transaction.calculateHash() = encodeRLP().keccak()