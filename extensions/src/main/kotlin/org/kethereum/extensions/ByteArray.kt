package org.kethereum.extensions

fun ByteArray.toFixedLengthByteArray(fixedSize: Int) = if (size == fixedSize) {
    this
} else {
    require(size < fixedSize) { "ByteArray too big - max size is $fixedSize" }
    ByteArray(fixedSize) { it -> getOrNull(size - fixedSize + it) ?: 0 }
}