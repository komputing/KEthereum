package org.kethereum.contract.abi.types.model.types

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ONE
import com.ionspin.kotlin.bignum.integer.util.fromTwosComplementByteArray
import org.kethereum.contract.abi.types.INT_BITS_CONSTRAINT
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.extensions.toBytesPadded
import org.komputing.khex.extensions.toNoPrefixHexString

class UIntETHType(override val paddedValue: ByteArray, params: BitsTypeParams) : ETHType<BigInteger> {

    init {
        INT_BITS_CONSTRAINT(params.bits)
        require(toKotlinType() < ONE shl (params.bits)) { "value ${toKotlinType()} must fit in ${params.bits} bits" }
    }

    override fun toKotlinType() = if (paddedValue.first() < 0) {
        BigInteger.parseString(paddedValue.toNoPrefixHexString(), 16)
    } else {
        BigInteger.fromTwosComplementByteArray(paddedValue)
    }

    companion object {
        fun ofNativeKotlinType(input: BigInteger, params: BitsTypeParams): UIntETHType {
            require(input.signum() >= 0) { "UInt must be positive" }
            return UIntETHType(input.toBytesPadded(ETH_TYPE_PAGESIZE), params)
        }

        fun ofPaginatedByteArray(input: PaginatedByteArray, params: BitsTypeParams) = input.nextPage()?.let { UIntETHType(it, params) }

        fun ofSting(string: String, params: BitsTypeParams) = ofNativeKotlinType(BigInteger.parseString(string), params)
    }

    override fun isDynamic() = false
}