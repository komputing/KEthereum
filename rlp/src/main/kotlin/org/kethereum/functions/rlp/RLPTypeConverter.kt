package org.kethereum.functions.rlp

import org.kethereum.extensions.removeLeadingZero
import org.kethereum.extensions.toMinimalByteArray
import java.math.BigInteger
import java.math.BigInteger.ZERO

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

// to RLP

fun String.toRLP() = RLPElement(toByteArray())

fun Int.toRLP() = RLPElement(toMinimalByteArray())
fun BigInteger.toRLP() = RLPElement(toByteArray().removeLeadingZero())
fun ByteArray.toRLP() = RLPElement(this)
fun Byte.toRLP() = RLPElement(kotlin.ByteArray(1, { this }))

// from RLP

fun RLPElement.toIntFromRLP() = bytes
        .mapIndexed { index, byte -> byte.toInt().shl((bytes.size - 1 - index) * 8) }
        .reduce { acc, i -> acc + i }

fun RLPElement.toBigIntegerFromRLP(): BigInteger = if (bytes.isEmpty()) ZERO else BigInteger(bytes)
fun RLPElement.toUnsignedBigIntegerFromRLP(): BigInteger = if (bytes.isEmpty()) ZERO else BigInteger(1, bytes)
fun RLPElement.toByteFromRLP(): Byte {
    if (bytes.size != 1) {
        throw IllegalArgumentException("trying to convert RLP with != 1 byte to Byte")
    }
    return bytes.first()
}

fun RLPElement.toStringFromRLP() = String(bytes)
