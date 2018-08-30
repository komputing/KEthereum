package org.kethereum.hashes

fun Int.rotateRight(distance: Int): Int {
    return this.ushr(distance) or (this shl -distance)
}

// TODO: Make this pure Kotlin
fun ByteArray.arrayCopy(srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    System.arraycopy(this, srcPos, dest, destPos, length)
}

// TODO: Make this pure Kotlin
fun IntArray.arrayCopy(srcPos: Int, dest: IntArray, destPos: Int, length: Int) {
    System.arraycopy(this, srcPos, dest, destPos, length)
}