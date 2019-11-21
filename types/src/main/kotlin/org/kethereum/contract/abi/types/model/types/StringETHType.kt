package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType

class StringETHType(override val paddedValue: ByteArray) : ETHType<String> {

    override fun toKotlinType() = String(DynamicSizedBytesETHType(paddedValue).toKotlinType())

    companion object {
        fun ofPaginatedByteArray(input: PaginatedByteArray) = DynamicSizedBytesETHType.ofPaginatedByteArray(input)?.let {
            StringETHType(it.paddedValue)
        }
        fun ofString(string: String) = StringETHType(DynamicSizedBytesETHType.ofNativeKotlinType(string.toByteArray()).paddedValue)
        fun ofNativeKotlinType(input: String) = ofString(input)
    }

    override fun isDynamic() = true
}