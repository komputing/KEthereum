package org.kethereum.extensions

fun Int.toByteArray() = ByteArray(4, { i -> shr(8 * (3 - i)).toByte() })
fun Int.toMinimalByteArray() = toByteArray().let { it.copyOfRange(it.minimalStart(), 4) }

private fun ByteArray.minimalStart() = indexOfFirst { it != 0.toByte() }.let { if (it == -1) 3 else it }
fun ByteArray.removeLeadingZero() = if (first() == 0.toByte()) copyOfRange(1, size) else this