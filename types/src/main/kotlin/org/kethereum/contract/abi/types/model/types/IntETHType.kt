package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.INT_BITS_CONSTRAINT
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.extensions.toFixedLengthByteArray
import java.math.BigInteger
import java.math.BigInteger.ONE

class IntETHType(override val paddedValue: ByteArray, params: String) : ETHType<BigInteger> {

    private val bits = BitsTypeParams.decodeFromString(params).bits

    init {
        INT_BITS_CONSTRAINT(bits)
        require(toKotlinType() < ONE.shiftLeft(bits-1)) { "value ${toKotlinType()} must fit in $bits bits" }
        require(toKotlinType() > -ONE.shiftLeft(bits-1)) { "value ${toKotlinType()} must fit in $bits bits" }
    }

    override fun toKotlinType() = BigInteger(paddedValue)

    override fun isDynamic() = false

    companion object {
        fun ofNativeKotlinType(input: BigInteger, params: String) =
                IntETHType(input.toByteArray().toFixedLengthByteArray(ETH_TYPE_PAGESIZE, if (input.signum() < 0) 0xFF.toByte() else 0), params)

        fun ofSting(string: String, params: String) = ofNativeKotlinType(BigInteger(string), params)

        fun ofPaginatedByteArray(input: PaginatedByteArray, params: String) = input.nextPage()?.let { IntETHType(it, params) }
    }
}