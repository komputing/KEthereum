package org.kethereum.contract.abi.types.model.types

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.rightPadToFixedPageSize
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

class DynamicSizedBytesETHType(override val paddedValue: ByteArray) : ETHType<ByteArray> {

    override fun toKotlinType(): ByteArray {
        val input = PaginatedByteArray(paddedValue)
        val len = UIntETHType.ofPaginatedByteArray(input, BitsTypeParams(256))
        return input.getBytes(len!!.toKotlinType().intValue(exactRequired = true))
    }

    companion object {
        fun ofPaginatedByteArray(input: PaginatedByteArray): DynamicSizedBytesETHType? {
            val pos = UIntETHType.ofPaginatedByteArray(input, BitsTypeParams(256))?.toKotlinType() ?: return null
            input.jumpTo(pos.intValue(exactRequired = true))
            val len = UIntETHType.ofPaginatedByteArray(input, BitsTypeParams(256)) ?: return null

            val array = len.toPaged().content + input.getBytes(len.toKotlinType().intValue(exactRequired = true)).rightPadToFixedPageSize(ETH_TYPE_PAGESIZE)
            input.endJump()
            return DynamicSizedBytesETHType(array)
        }

        fun ofNativeKotlinType(input: ByteArray) = DynamicSizedBytesETHType(UIntETHType.ofNativeKotlinType(BigInteger(input.size), BitsTypeParams(256)).paddedValue + input.rightPadToFixedPageSize(ETH_TYPE_PAGESIZE))

        fun ofString(input: String) = ofNativeKotlinType(HexString(input).hexToByteArray())
    }

    override fun isDynamic() = true

}
