package org.kethereum.functions.rlp

import java.util.*

/**
RLP as of Appendix B. Recursive Length Prefix at https://github.com/ethereum/yellowpaper
 */

interface RLPType

data class RLPElement(val bytes: ByteArray) : RLPType {
    override fun equals(other: Any?) = when (other) {
        is RLPElement -> Arrays.equals(bytes, other.bytes)
        else -> false
    }

    override fun hashCode() = Arrays.hashCode(bytes)
}

data class RLPList(val element: List<RLPType>) : RLPType