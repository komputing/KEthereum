package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.extensions.newStack
import org.kethereum.extensions.pop
import org.kethereum.extensions.push
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString


class PaginatedByteArray(val content: ByteArray, private val pageSize: Int = ETH_TYPE_PAGESIZE) {

    constructor(input: HexString?, pageSize: Int = ETH_TYPE_PAGESIZE)
            : this(input?.hexToByteArray() ?: ByteArray(0), pageSize)

    init {
        require(content.size % pageSize == 0) { "Input size (${content.size} must be a multiple of pageSize ($pageSize)" }
    }

    private var currentOffset = 0
    private var jumStack = newStack<Int>()

    fun jumpTo(pos: Int) {
        jumStack.push(currentOffset)
        currentOffset = pos
    }

    fun endJump() {
        currentOffset = jumStack.pop()!!
    }

    fun getBytes(count: Int) = content.sliceArray(currentOffset until currentOffset + count)

    fun nextPage() = if (currentOffset < content.size) {
        content.sliceArray(currentOffset until currentOffset + pageSize).also {
            currentOffset += pageSize
        }
    } else null

}