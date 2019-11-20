package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.rightPadToFixedSize
import org.walleth.khex.hexToByteArray

class BytesETHType(override val paddedValue: ByteArray, paramsString: String) : ETHType<ByteArray> {

    private val params = BytesTypeParams.decodeFromString(paramsString)

    init {
        require(params.bytes <= ETH_TYPE_PAGESIZE) { "Maximum size is $ETH_TYPE_PAGESIZE - but got ${params.bytes} " }
    }

    override fun toKotlinType() = paddedValue.sliceArray(0 until params.bytes)

    companion object {
        fun ofPaginatedByteArray(input: PaginatedByteArray, params: String) = input.nextPage()?.let { BytesETHType(it, params) }

        fun ofNativeKotlinType(input: ByteArray, params: String) = BytesETHType(input.rightPadToFixedSize(ETH_TYPE_PAGESIZE), params)

        fun ofString(string: String, params: String) = ofNativeKotlinType(string.hexToByteArray(), params)
    }

    override fun isDynamic() = false
}