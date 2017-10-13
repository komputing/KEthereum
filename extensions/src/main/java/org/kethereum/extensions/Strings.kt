package org.kethereum.extensions



fun String.has0xPrefix() = startsWith("0x")
fun String.prepend0xPrefix() = if (has0xPrefix()) this else "0x$this"
fun String.clean0xPrefix() = if (has0xPrefix()) this.substring(2) else this