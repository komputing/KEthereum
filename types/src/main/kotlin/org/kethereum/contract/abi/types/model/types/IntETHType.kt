package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.INT_BITS_CONSTRAINT
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.extensions.toFixedLengthByteArray
import java.math.BigInteger
import java.math.BigInteger.ONE

class IntETHType(override val paddedValue: ByteArray, params: BitsTypeParams) : ETHType<BigInteger> {


    init {
        INT_BITS_CONSTRAINT(params.bits)
        require(toKotlinType() < ONE.shiftLeft(params.bits-1)) { "value ${toKotlinType()} must fit in ${params.bits} bits" }
        require(toKotlinType() > -ONE.shiftLeft(params.bits-1)) { "value ${toKotlinType()} must fit in $params.bits} bits" }
    }

    override fun toKotlinType() = BigInteger(paddedValue)

    override fun isDynamic() = false

    companion object {
        fun ofNativeKotlinType(input: BigInteger, params: BitsTypeParams) =
                IntETHType(input.toByteArray().toFixedLengthByteArray(ETH_TYPE_PAGESIZE, if (input.signum() < 0) 0xFF.toByte() else 0), params)

        fun ofSting(string: String, params: BitsTypeParams) = ofNativeKotlinType(BigInteger(string), params)

        fun ofPaginatedByteArray(input: PaginatedByteArray, params: BitsTypeParams) = input.nextPage()?.let { IntETHType(it, params) }
    }
}