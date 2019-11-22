package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETHTypeParams
import java.math.BigInteger

class IntETHType(override val paddedValue: ByteArray, params: ETHTypeParams) : ETHType<Int> {

    override fun toKotlinType(): Int {
        TODO()
    }

    override fun isDynamic() = TODO()

    companion object {
        fun ofNativeKotlinType(input: BigInteger, params: String): IntETHType {
            TODO()
        }

        fun ofSting(string: String, params: String) = ofNativeKotlinType(BigInteger(string), params)

    }
}