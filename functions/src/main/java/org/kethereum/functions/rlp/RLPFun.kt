package org.kethereum.functions.rlp

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

val element_offset = 128
val list_offset = 192

fun RLPType.encode(): ByteArray = when (this) {
    is RLPElement -> bytes.encode(element_offset)
    is RLPList -> element.map { it.encode() }
            .fold(ByteArray(0), { acc, bytes -> acc + bytes }) // this can be speed optimized when needed
            .encode(list_offset)
    else -> throw (IllegalArgumentException("RLPType must be RLPElement or RLPList"))
}

private fun ByteArray.encode(offset: Int) = when {
    size == 1 && (first().toInt() and 0xff < 128) && offset == element_offset -> this
    size <= 55 -> ByteArray(1, { (size + offset).toByte() }).plus(this)
    else -> size.toMinimalByteArray().let { arr ->
        ByteArray(1, { (offset + 0x37 + arr.size).toByte() }) + arr + this
    }
}

fun String.toRLP() = RLPElement(toByteArray())
fun Int.toRLP() = RLPElement(toMinimalByteArray())

fun Int.toByteArray() = ByteArray(4, { i -> shr(8 * (3 - i)).toByte() })
internal fun Int.toMinimalByteArray() = toByteArray().let { it.copyOfRange(it.minimalStart(), 4) }
private fun ByteArray.minimalStart() = indexOfFirst { it != 0.toByte() }.let { if (it == -1) 4 else it }
