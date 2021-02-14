package org.kethereum.extensions

fun Int.toByteArray() = ByteArray(4) { i ->
    shr(8 * (3 - i)).toByte()
}

fun Int.toMinimalByteArray() = toByteArray().removeLeadingZeros()