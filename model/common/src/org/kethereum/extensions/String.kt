package org.kethereum.extensions

import org.kethereum.number.BigInteger

fun String.hexToByteArray(): ByteArray {
    if (length % 2 != 0)
        throw IllegalArgumentException("hex-string must have an even number of digits (nibbles)")

    val cleanInput = if (startsWith("0x")) substring(2) else this

    return ByteArray(cleanInput.length / 2).apply {
        var i = 0
        while (i < cleanInput.length) {
            this[i / 2] = ((cleanInput[i].getNibbleValue() shl 4) + cleanInput[i + 1].getNibbleValue()).toByte()
            i += 2
        }
    }
}

private fun Char.getNibbleValue() = this.digit(16).also {
    if (it == -1) throw IllegalArgumentException("Not a valid hex char: $this")
}

fun String.hexToBigInteger() = BigInteger(clean0xPrefix(), 16)

fun String.maybeHexToBigInteger() = if (has0xPrefix()) {
    BigInteger(clean0xPrefix(), 16)
} else {
    BigInteger(this)
}

fun String.has0xPrefix() = startsWith("0x")
fun String.prepend0xPrefix() = if (has0xPrefix()) this else "0x$this"
fun String.clean0xPrefix() = if (has0xPrefix()) this.substring(2) else this