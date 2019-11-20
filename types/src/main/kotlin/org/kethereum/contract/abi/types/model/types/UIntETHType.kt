package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.extensions.toBytesPadded
import java.math.BigInteger
import java.math.BigInteger.ONE

class UIntETHType(override val paddedValue: ByteArray, params: String) : ETHType<BigInteger> {

    private val bits = BitsTypeParams.decodeFromString(params).bits

    init {
        require(bits > 0) { "Must have at least 8 bits - but got $bits" }
        require(bits <= 256) { "256 bits is the max - but got $bits" }
        require(bits % 8 == 0) { "bits % 8 must be 0 - not the case for $bits" }
        require(toKotlinType() < ONE.shiftLeft(bits)) { "value ${toKotlinType()} must fit in $bits bit" }
    }

    override fun toKotlinType() = BigInteger(paddedValue)

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