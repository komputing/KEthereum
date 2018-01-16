package org.kethereum.functions.rlp

import org.kethereum.extensions.removeLeadingZero
import org.kethereum.extensions.toMinimalByteArray
import java.math.BigInteger

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

const val ELEMENT_OFFSET = 128
const val LIST_OFFSET = 192

fun RLPType.encode(): ByteArray = when (this) {
    is RLPElement -> bytes.encode(ELEMENT_OFFSET)
    is RLPList -> element.map { it.encode() }
            .fold(ByteArray(0), { acc, bytes -> acc + bytes }) // this can be speed optimized when needed
            .encode(LIST_OFFSET)
    else -> throw (IllegalArgumentException("RLPType must be RLPElement or RLPList"))
}

private fun ByteArray.encode(offset: Int) = when {
    size == 1 && (first().toInt() and 0xff < 128) && offset == ELEMENT_OFFSET -> this
    size <= 55 -> ByteArray(1, { (size + offset).toByte() }).plus(this)
    else -> size.toMinimalByteArray().let { arr ->
        ByteArray(1, { (offset + 0x37 + arr.size).toByte() }) + arr + this
    }
}

fun String.toRLP() = RLPElement(toByteArray())
fun Int.toRLP() = RLPElement(toMinimalByteArray())
fun BigInteger.toRLP() = RLPElement(toByteArray().removeLeadingZero())
fun ByteArray.toRLP() = RLPElement(this)
fun Byte.toRLP() = RLPElement(kotlin.ByteArray(1, { this }))
