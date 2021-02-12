package org.kethereum.extensions

fun ByteArray.toFixedLengthByteArray(fixedSize: Int, fillByte: Byte = 0) = if (size == fixedSize) {
    this
} else {
    require(size < fixedSize) { "ByteArray too big - max size is $fixedSize but got $size" }
    ByteArray(fixedSize) { getOrNull(size - fixedSize + it) ?: fillByte }
}

fun ByteArray.removeLeadingZeros() = this.copyOfRange(this.minimalStart(), this.size)
private fun ByteArray.minimalStart() = indexOfFirst { it != 0.toByte() }.let { if (it == -1) size else it }
