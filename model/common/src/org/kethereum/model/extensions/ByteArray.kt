package org.kethereum.model.extensions

import org.kethereum.model.number.BigInteger

fun ByteArray.toBigInteger(offset: Int, length: Int) = BigInteger(1, this.copyOfRange(offset, offset + length))
fun ByteArray.toBigInteger() = BigInteger(1, this)

fun ByteArray.copy(srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    val temp = this.copyOf()
    (0 until length).forEach { dest[destPos + it] = temp[srcPos + it] }
}

fun ByteArray.toHexString(prefix: String = "0x") = prefix + joinToString("") { it.toHexString() }

fun ByteArray.toNoPrefixHexString() = toHexString("")