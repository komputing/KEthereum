package org.kethereum.rlp

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ZERO
import com.ionspin.kotlin.bignum.integer.Sign
import org.kethereum.extensions.removeLeadingZero
import org.kethereum.extensions.toMinimalByteArray

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

// to RLP

fun String.toRLP() = RLPElement(toByteArray())

fun Int.toRLP() = RLPElement(toMinimalByteArray())
fun BigInteger.toRLP() = RLPElement(toByteArray().removeLeadingZero())
fun ByteArray.toRLP() = RLPElement(this)
fun Byte.toRLP() = RLPElement(ByteArray(1) { this })

// from RLP

fun RLPElement.toIntFromRLP() = if (bytes.isEmpty()) {
    0
} else {
    bytes.mapIndexed { index, byte -> (byte.toInt() and 0xff).shl((bytes.size - 1 - index) * 8) }
            .reduce { acc, i -> acc + i }
}

fun RLPElement.toUnsignedBigIntegerFromRLP(): BigInteger = if (bytes.isEmpty()) ZERO else BigInteger.fromByteArray(bytes, Sign.POSITIVE)
fun RLPElement.toByteFromRLP(): Byte {
    require(bytes.size == 1) { "trying to convert RLP with != 1 byte to Byte" }
    return bytes.first()
}

fun RLPElement.toStringFromRLP() = String(bytes)
