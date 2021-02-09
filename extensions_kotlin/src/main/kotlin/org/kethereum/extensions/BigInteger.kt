package org.kethereum.extensions

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import org.komputing.khex.extensions.clean0xPrefix
import org.komputing.khex.extensions.has0xPrefix
import org.komputing.khex.model.HexString

fun BigInteger.toBytesPadded(length: Int): ByteArray {
    val result = ByteArray(length)
    val bytes = toByteArray()

    val offset = if (bytes[0].toInt() == 0) 1 else 0

    if (bytes.size - offset > length) {
        throw RuntimeException("Input is too large to put in byte array of size $length")
    }

    val destOffset = length - bytes.size + offset
    System.arraycopy(bytes, offset, result, destOffset, bytes.size - offset)
    return result
}

fun BigInteger.toHexStringNoPrefix(): String = toString(16)
fun BigInteger.toHexString(): String = "0x" + toString(16)

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

fun HexString.hexToBigInteger() = BigInteger.parseString(clean0xPrefix().string, 16)

fun HexString.maybeHexToBigInteger() = if (has0xPrefix()) {
    BigInteger.parseString(clean0xPrefix().string, 16)
} else {
    BigInteger.parseString(string)
}

fun ByteArray.toBigInteger(offset: Int, length: Int) = BigInteger.fromByteArray(copyOfRange(offset, offset + length), Sign.POSITIVE)
fun ByteArray.toBigInteger() = BigInteger.fromByteArray(this, Sign.POSITIVE)

