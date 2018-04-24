package org.kethereum.functions.rlp

import java.math.BigInteger

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

fun ByteArray.decodeRLP() = decodeRLPWithSize().element

private data class DecodeResult(val element: RLPType, val size: Int)

private data class LengthAndOffset(var length: Int, var offset: Int)

private fun ByteArray.decodeRLPWithSize(offset: Int = 0): DecodeResult {

    val value = this[offset].toInt() and 0xFF
    return when {
        value < ELEMENT_OFFSET -> DecodeResult(value.toRLP(), 1)
        value < LIST_OFFSET -> (value - ELEMENT_OFFSET).let {
            val lengthAndOffset = getLengthAndOffset(it, offset)
            DecodeResult(copyOfRange(lengthAndOffset.offset, lengthAndOffset.offset + lengthAndOffset.length).toRLP(), lengthAndOffset.length + 1)
        }
        else -> (value - LIST_OFFSET).let {
            val list = mutableListOf<RLPType>()

            val lengthAndOffset = getLengthAndOffset(it, offset)

            while (lengthAndOffset.offset <= offset + lengthAndOffset.length) {
                val element = decodeRLPWithSize(lengthAndOffset.offset)
                lengthAndOffset.offset += element.size
                list.add(element.element)
            }
            DecodeResult(RLPList(list), lengthAndOffset.offset - offset)
        }
    }
}

private fun ByteArray.getLengthAndOffset(firstByte: Int, offset: Int) = if (firstByte <= 55) {
    LengthAndOffset(firstByte, offset + 1)
} else {
    val size = firstByte - 54
    LengthAndOffset(BigInteger(copyOfRange(offset + 1, offset + size)).toInt(), offset + size)
}