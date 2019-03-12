package org.kethereum.extensions

import org.kethereum.number.BigInteger

fun ByteArray.toBigInteger(offset: Int, length: Int) = BigInteger(1, this.copyOfRange(offset, offset + length))
fun ByteArray.toBigInteger() = BigInteger(1, this)

fun ByteArray.copy(srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    val temp = this.copyOf()
    (0 until length).forEach { dest[destPos + it] = temp[srcPos + it] }
}