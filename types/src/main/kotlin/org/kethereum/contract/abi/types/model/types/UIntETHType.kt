package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.INT_BITS_CONSTRAINT
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.extensions.toBytesPadded
import org.walleth.khex.toNoPrefixHexString
import java.math.BigInteger
import java.math.BigInteger.ONE

class UIntETHType(override val paddedValue: ByteArray, params: String) : ETHType<BigInteger> {

    private val bits = BitsTypeParams.decodeFromString(params).bits

    init {
        INT_BITS_CONSTRAINT(bits)
        require(toKotlinType() < ONE.shiftLeft(bits)) { "value ${toKotlinType()} must fit in $bits bits" }
    }

    override fun toKotlinType() = if (paddedValue.first() < 0) {
        BigInteger(paddedValue.toNoPrefixHexString(), 16)
    } else {
        BigInteger(paddedValue)
    }

    companion object {
        fun ofNativeKotlinType(input: BigInteger, params: String): UIntETHType {
            require(input.signum() >= 0) { "UInt must be positive" }
            return UIntETHType(input.toBytesPadded(ETH_TYPE_PAGESIZE), params)
        }

        fun ofPaginatedByteArray(input: PaginatedByteArray, params: String) = input.nextPage()?.let { UIntETHType(it, params) }

        fun ofSting(string: String, params: String) = ofNativeKotlinType(BigInteger(string), params)
    }

    override fun isDynamic() = false
}