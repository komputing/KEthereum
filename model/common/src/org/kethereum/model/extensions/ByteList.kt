package org.kethereum.model.extensions

fun List<Byte>.toHexString(prefix: String = "0x") = toByteArray().toHexString(prefix)
fun List<Byte>.toNoPrefixHexString() = toHexString("")