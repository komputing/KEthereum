package org.kethereum.functions.rlp

import org.kethereum.extensions.toMinimalByteArray

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

fun RLPType.encode(): ByteArray = when (this) {
    is RLPElement -> bytes.encodeRLP(ELEMENT_OFFSET)
    is RLPList -> element.map { it.encode() }
            .fold(ByteArray(0), { acc, bytes -> acc + bytes }) // this can be speed optimized when needed
            .encodeRLP(LIST_OFFSET)
}

internal fun ByteArray.encodeRLP(offset: Int) = when {
    size == 1 && ((first().toInt() and 0xff) < ELEMENT_OFFSET) && offset == ELEMENT_OFFSET -> this
    size <= 55 -> ByteArray(1, { (size + offset).toByte() }).plus(this)
    else -> size.toMinimalByteArray().let { arr ->
        ByteArray(1, { (offset + 55 + arr.size).toByte() }) + arr + this
    }
}
