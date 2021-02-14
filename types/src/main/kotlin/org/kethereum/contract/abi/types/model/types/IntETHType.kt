package org.kethereum.contract.abi.types.model.types

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ONE
import com.ionspin.kotlin.bignum.integer.util.fromTwosComplementByteArray
import com.ionspin.kotlin.bignum.integer.util.toTwosComplementByteArray
import org.kethereum.contract.abi.types.INT_BITS_CONSTRAINT
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.extensions.toFixedLengthByteArray

class IntETHType(override val paddedValue: ByteArray, params: BitsTypeParams) : ETHType<BigInteger> {


    init {
        INT_BITS_CONSTRAINT(params.bits)
        require(toKotlinType() < ONE shl (params.bits-1)) { "value ${toKotlinType()} must fit in ${params.bits} bits" }
        require(toKotlinType() > -ONE shl (params.bits-1)) { "value ${toKotlinType()} must fit in $params.bits} bits" }
    }

    override fun toKotlinType() = BigInteger.fromTwosComplementByteArray(paddedValue)

    override fun isDynamic() = false

    companion object {
        fun ofNativeKotlinType(input: BigInteger, params: BitsTypeParams) =
                IntETHType(input.toTwosComplementByteArray().toFixedLengthByteArray(ETH_TYPE_PAGESIZE, if (input.signum() < 0) 0xFF.toByte() else 0), params)

        fun ofSting(string: String, params: BitsTypeParams) = ofNativeKotlinType(BigInteger.parseString(string), params)

        fun ofPaginatedByteArray(input: PaginatedByteArray, params: BitsTypeParams) = input.nextPage()?.let { IntETHType(it, params) }
    }
}