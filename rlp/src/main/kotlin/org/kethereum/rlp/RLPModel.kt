package org.kethereum.rlp

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

internal const val ELEMENT_OFFSET = 128
internal const val LIST_OFFSET = 192

sealed class RLPType

data class RLPElement(val bytes: ByteArray) : RLPType() {

    override fun equals(other: Any?) = when (other) {
        is RLPElement -> bytes.contentEquals(other.bytes)
        else -> false
    }

    override fun hashCode() = bytes.contentHashCode()
}

data class RLPList(val element: List<RLPType>) : RLPType()

class IllegalRLPException(msg: String) : IllegalArgumentException(msg)