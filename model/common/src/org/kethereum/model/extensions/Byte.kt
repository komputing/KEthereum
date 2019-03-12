package org.kethereum.model.extensions

/**
 *  chars for nibble
 */
private const val CHARS = "0123456789abcdef"

/**
 *  Returns 2 char hex string for Byte
 */
fun Byte.toHexString() = toInt().let {
    CHARS[it.shr(4) and 0x0f].toString() + CHARS[it.and(0x0f)].toString()
}