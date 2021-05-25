package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE

class BoolETHType(override val paddedValue: ByteArray) : ETHType<Boolean> {

    override fun toKotlinType() = paddedValue[31] > 0

    companion object {

        fun ofPaginatedByteArray(input: PaginatedByteArray) = input.nextPage()?.let { BoolETHType(it) }

        fun ofNativeKotlinType(input: Boolean) = BoolETHType(ByteArray(ETH_TYPE_PAGESIZE) {
            if (input && it == 31) 1 else 0
        })

        fun ofString(input: String) = ofNativeKotlinType(when (input.lowercase()) {
            "true" -> true
            "false" -> false
            else -> throw IllegalArgumentException("boolean must be true or false")
        })
    }

    override fun isDynamic() = false
}