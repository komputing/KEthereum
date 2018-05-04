package org.kethereum.extensions

import org.walleth.khex.clean0xPrefix
import org.walleth.khex.has0xPrefix
import java.math.BigInteger
import java.util.*

fun BigInteger.toBytesPadded(length: Int): ByteArray {
    val result = ByteArray(length)
    val bytes = toByteArray()

    val bytesLength: Int
    val srcOffset: Int
    if (bytes[0].toInt() == 0) {
        bytesLength = bytes.size - 1
        srcOffset = 1
    } else {
        bytesLength = bytes.size
        srcOffset = 0
    }

    if (bytesLength > length) {
        throw RuntimeException("Input is too large to put in byte array of size $length")
    }

    val destOffset = length - bytesLength
    System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength)
    return result
}

fun BigInteger.toHexStringNoPrefix(): String = toString(16)

fun BigInteger.toHexStringZeroPadded(size: Int, withPrefix: Boolean = true): String {
    var result = toHexStringNoPrefix()

    val length = result.length
    if (length > size) {
        throw UnsupportedOperationException("Value $result is larger then length $size")
    } else if (signum() < 0) {
        throw UnsupportedOperationException("Value cannot be negative")
    }

    if (length < size) {
        result = "0".repeat(size - length) + result
    }

    return if (withPrefix) {
        "0x$result"
    } else {
        result
    }
}

fun String.hexToBigInteger() = BigInteger(clean0xPrefix(), 16)

fun String.maybeHexToBigInteger() = if (has0xPrefix()) {
    BigInteger(clean0xPrefix(), 16)
} else {
    BigInteger(this)
}

fun ByteArray.toBigInteger(offset: Int, length: Int) = BigInteger(1, Arrays.copyOfRange(this, offset, offset + length))
fun ByteArray.toBigInteger() = BigInteger(1, this)

