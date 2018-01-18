package org.kethereum.extensions

import kotlin.experimental.or

fun ByteArray.toBitArray(): BooleanArray {
    val bits = BooleanArray(this.size * 8)
    for (byteIndex in this.indices)
        for (bitIndex in 0..7) {
            bits[byteIndex * 8 + bitIndex] = (1 shl (7 - bitIndex)) and this[byteIndex].toInt() != 0
        }
    return bits
}

fun BooleanArray.toByteArray(): ByteArray {
    val result = ByteArray(this.size / 8)
    for (byteIndex in result.indices)
        for (bitIndex in 0..7)
            if (this[byteIndex * 8 + bitIndex]) {
                result[byteIndex] = result[byteIndex] or (1 shl (7 - bitIndex)).toByte()
            }
    return result
}