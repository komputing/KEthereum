package org.kethereum.functions

fun List<Byte>.startsWith(prefix: List<Byte>): Boolean {
    if (prefix.size > this.size)
        return false
    (0..(prefix.size) - 1).forEach {
        if (this[it] != prefix[it])
            return false
    }
    return true
}