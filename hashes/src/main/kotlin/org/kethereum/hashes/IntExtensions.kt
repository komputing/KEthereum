package org.kethereum.hashes

val INT_BYTES = 4

fun Int.rotateRight(distance: Int): Int {
    return this.ushr(distance) or (this shl -distance)
}

fun Int.toBytes(): Array<Byte> {
    val result = ByteArray(4)
    result[0] = (this shr 24).toByte()
    result[1] = (this shr 16).toByte()
    result[2] = (this shr 8).toByte()
    result[3] = this.toByte()
    return result.toTypedArray()
}