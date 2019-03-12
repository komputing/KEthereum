package org.kethereum.model.extensions

actual fun IntArray.fillWith(value: Int) {
    return this.fill(value)
}