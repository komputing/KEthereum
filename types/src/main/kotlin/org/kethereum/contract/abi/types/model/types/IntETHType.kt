package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETHTypeParams

class IntETHType(override val paddedValue: ByteArray, params: ETHTypeParams) : ETHType<Int> {

    override fun toKotlinType(): Int {
        TODO()
    }

}