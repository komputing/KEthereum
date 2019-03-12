package org.kethereum.functions.rlp

import kotlinx.io.core.String
import kotlinx.io.core.toByteArray
import org.kethereum.model.extensions.removeLeadingZero
import org.kethereum.model.extensions.toMinimalByteArray
import org.kethereum.model.number.BigInteger
import org.kethereum.model.number.BigInteger.Companion.ZERO

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

// to RLP

fun String.toRLP() = RLPElement(toByteArray())

fun Int.toRLP() = RLPElement(toMinimalByteArray())
fun BigInteger.toRLP() = RLPElement(toByteArray().removeLeadingZero())
fun ByteArray.toRLP() = RLPElement(this)
fun Byte.toRLP() = RLPElement(kotlin.ByteArray(1) { this })

// from RLP

fun RLPElement.toIntFromRLP() = if (bytes.isEmpty()) {
    0
} else {
    bytes.mapIndexed { index, byte -> (byte.toInt() and 0xff).shl((bytes.size - 1 - index) * 8) }
            .reduce { acc, i -> acc + i }
}

fun RLPElement.toUnsignedBigIntegerFromRLP(): BigInteger = if (bytes.isEmpty()) ZERO else BigInteger(1, bytes)
fun RLPElement.toByteFromRLP(): Byte {
    if (bytes.size != 1) {
        throw IllegalArgumentException("trying to convert RLP with != 1 byte to Byte")
    }
    return bytes.first()
}

fun RLPElement.toStringFromRLP() = String(bytes)
