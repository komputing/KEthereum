package org.kethereum.functions

fun List<Byte>.startsWith(prefix: List<Byte>)
        = size >= prefix.size &&
        (0..(prefix.size - 1)).all { this[it] == prefix[it] }