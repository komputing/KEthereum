package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.walleth.khex.hexToByteArray
import java.util.*

class PaginatedByteArray(val content: ByteArray, private val pageSize: Int = ETH_TYPE_PAGESIZE) {

    constructor(input: String?) : this(input?.hexToByteArray() ?: ByteArray(0))

    init {
        require(content.size % pageSize == 0) { "Input size (${content.size} must be a multiple of pageSize ($pageSize)" }
    }

    private var currentOffset = 0
    private var jumStack = Stack<Int>()

    fun jumpTo(pos: Int) {
        jumStack.add(currentOffset)
        currentOffset = pos
    }

    fun endJump() {
        currentOffset = jumStack.pop()
    }

    fun getBytes(count: Int) = content.sliceArray(currentOffset until currentOffset + count)

    fun nextPage() = if (currentOffset < content.size) {
        content.sliceArray(currentOffset until currentOffset + pageSize).also {
            currentOffset += pageSize
        }
    } else null

    companion object {
        fun ofHEX(input: String, pageSize: Int = 32) = PaginatedByteArray(input.hexToByteArray(), pageSize)
    }
}