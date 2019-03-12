package org.kethereum.model.extensions

fun IntArray.copy(srcPos: Int, dest: IntArray, destPos: Int, length: Int) {
    val temp = this.copyOf()
    (0 until length).forEach { dest[destPos + it] = temp[srcPos + it] }
}

expect fun IntArray.fillWith(value: Int)