package org.kethereum.hashes

const val INT_BYTES = 4


/**
 * Returns the value obtained by rotating the two's complement binary representation of the specified [Int] value
 * right by the specified number of bits.
 * (Bits shifted out of the right hand, or low-order, side reenter on the left, or high-order.)
 */
internal fun Int.rotateRight(distance: Int): Int {
    return this.ushr(distance) or (this shl -distance)
}


/**
 * Converts an [Int] to an array of [Byte] using the big-endian conversion.
 * (The [Int] will be converted into 4 bytes)
 */
internal fun Int.toBytes(): Array<Byte> {
    val result = ByteArray(INT_BYTES)
    result[0] = (this shr 24).toByte()
    result[1] = (this shr 16).toByte()
    result[2] = (this shr 8).toByte()
    result[3] = this.toByte()
    return result.toTypedArray()
}