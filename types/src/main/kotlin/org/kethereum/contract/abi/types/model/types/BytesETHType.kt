package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.rightPadToFixedSize
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

class BytesETHType(override val paddedValue: ByteArray, val params: BytesTypeParams) : ETHType<ByteArray> {

    init {
        require(params.bytes <= ETH_TYPE_PAGESIZE) { "Maximum size is $ETH_TYPE_PAGESIZE - but got ${params.bytes} " }
    }

    override fun toKotlinType() = paddedValue.sliceArray(0 until params.bytes)

    companion object {
        fun ofPaginatedByteArray(input: PaginatedByteArray, params: BytesTypeParams) = input.nextPage()?.let { BytesETHType(it, params) }

        fun ofNativeKotlinType(input: ByteArray, params: BytesTypeParams) = BytesETHType(input.rightPadToFixedSize(ETH_TYPE_PAGESIZE), params)

        fun ofString(string: String, params: BytesTypeParams) = ofNativeKotlinType(HexString(string).hexToByteArray(), params)
    }

    override fun isDynamic() = false
}