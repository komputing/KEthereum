package org.kethereum.functions.rlp

import java.math.BigInteger

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

fun ByteArray.decodeRLP() = decodeRLPWithSize().element

private data class DecodeResult(val element: RLPType, val size: Int)

private data class LengthAndOffset(val length: Int, val offset: Int)

private fun ByteArray.decodeRLPWithSize(offset: Int = 0): DecodeResult {

    if (offset >= size) {
        throw IllegalRLPException("Cannot decode RLP at offset=$offset and size=$size")
    }

    val value = this[offset].toInt() and 0xFF
    return when {
        value < ELEMENT_OFFSET -> DecodeResult(value.toRLP(), 1)
        value < LIST_OFFSET -> (value - ELEMENT_OFFSET).let {
            val lengthAndOffset = getLengthAndOffset(it, offset)
            DecodeResult(copyOfRange(lengthAndOffset.offset, lengthAndOffset.offset + lengthAndOffset.length).toRLP(), lengthAndOffset.length + lengthAndOffset.offset - offset)
        }
        else -> (value - LIST_OFFSET).let {
            val list = mutableListOf<RLPType>()

            val lengthAndOffset = getLengthAndOffset(it, offset)
            var currentOffset = lengthAndOffset.offset
            while (currentOffset < lengthAndOffset.offset + lengthAndOffset.length) {
                val element = decodeRLPWithSize(currentOffset)
                currentOffset += element.size
                list.add(element.element)
            }
            DecodeResult(RLPList(list), (lengthAndOffset.offset + lengthAndOffset.length) - offset)
        }
    }
}

private fun ByteArray.getLengthAndOffset(firstByte: Int, offset: Int) = if (firstByte <= 55) {
    LengthAndOffset(firstByte, offset + 1)
} else {
    val size = firstByte - 54
    val length = BigInteger(1, copyOfRange(offset + 1, offset + size)).toInt()
    LengthAndOffset(length, offset + size)
}