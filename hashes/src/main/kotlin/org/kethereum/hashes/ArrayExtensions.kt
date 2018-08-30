package org.kethereum.hashes

import java.nio.ByteBuffer

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

// TODO: Make this pure Kotlin
fun IntArray.arrayCopy(srcPos: Int, dest: IntArray, destPos: Int, length: Int) {
    System.arraycopy(this, srcPos, dest, destPos, length)
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

    // TODO: Once the following code works, replace it with the one below
//        val array = IntArray(this.size / 4)
//        for (i in array.indices) {
//            val integer = arrayOf(this[i*4], this[i*4+1], this[i*4+2], this[i*4+3])
//            array[i] = integer.toInt()
//        }
//        return array

    val buf = ByteBuffer.wrap(this)
    val result = IntArray(this.size / Integer.BYTES)
    for (i in result.indices) {
        result[i] = buf.int
    }
    return result
}

// TODO: Make this pure Kotlin
fun ByteArray.arrayCopy(srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    System.arraycopy(this, srcPos, dest, destPos, length)
}

/**
 * Converts 4 bytes into their integer representation following the big-endian conversion.
 */
fun Array<Byte>.toInt(): Int {
    return (this[0].toInt() shl 24) +
            (this[1].toInt() shl 16) +
            (this[2].toInt() shl 8) +
            (this[3].toInt() shl 0)
}



