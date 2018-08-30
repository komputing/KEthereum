package org.kethereum.hashes

// INT ARRAY

/**
 * Converts the given int array into a byte array via big-endian conversion
 * (1 int becomes 4 bytes).
 * @return The converted array.
 */
fun IntArray.toByteArray(): ByteArray {
    val array = ByteArray(this.size * 4)
    for (i in this.indices) {
        val bytes = this[i].toBytes()
        array[i * 4] = bytes[0]
        array[i * 4 + 1] = bytes[1]
        array[i * 4 + 2] = bytes[2]
        array[i * 4 + 3] = bytes[3]
    }
    return array
}

/**
 * Copies [length] elements present inside [this] starting from [srcPos] into [dest] starting from [destPos].
 * Thanks to manu4606
 */
fun IntArray.arrayCopy(srcPos: Int, dest: IntArray, destPos: Int, length: Int) {
    // Make a deep copy avoiding errors when this == dest
    val temp = this.copyOf()
    (0 until length).forEach { dest[destPos + it] = temp[srcPos + it] }
}

// BYTE ARRAY

/**
 * Converts the given byte array into an int array via big-endian conversion
 * (4 bytes become 1 int).
 * @return The converted array.
 */
fun ByteArray.toIntArray(): IntArray {
    if (this.size % Integer.BYTES != 0) {
        throw IllegalArgumentException("byte array length")
    }

    val array = IntArray(this.size / 4)
    for (i in array.indices) {
        val integer = arrayOf(this[i*4], this[i*4+1], this[i*4+2], this[i*4+3])
        array[i] = integer.toInt()
    }
    return array
}

/**
 * Copies [length] elements present inside [this] starting from [srcPos] into [dest] starting from [destPos].
 * Thanks to manu4606
 */
fun ByteArray.arrayCopy(srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    // Make a deep copy avoiding errors when this == dest
    val temp = this.copyOf()
    (0 until length).forEach { dest[destPos + it] = temp[srcPos + it] }
}

/**
 * Converts 4 bytes into their integer representation following the big-endian conversion.
 */
fun Array<Byte>.toInt(): Int {
    return (this[0].toUInt() shl 24) + (this[1].toUInt() shl 16) + (this[2].toUInt() shl 8) + (this[3].toUInt() shl 0)
}

/**
 * Convert a Byte into an unsigned Int.
 * Source: https://stackoverflow.com/questions/38651192/how-to-correctly-handle-byte-values-greater-than-127-in-kotlin
 */
fun Byte.toUInt() = when {
    (toInt() < 0) -> 255 + toInt() + 1
    else -> toInt()
}

/**
 * Writes a long split into 8 bytes.
 * @param [offset] start index
 * @param [value] the value to insert
 * Thanks to manu4606
 */
fun ByteArray.putLong(offset: Int, value: Long) {
    for(i in 7 downTo 0) {
        val temp =  ((value ushr (i *  8)) and 0xff).toInt()
        this[offset + 7 - i] = temp.toUByte()
    }
}

/**
 * Converts an [Int] to an unsigned [Byte].
 * Thanks to manu4606
 */
fun Int.toUByte(): Byte  = when {
    (this < 128) -> toByte()
    else -> (-this + 127).toByte()
}


