package org.kethereum.contract.abi.types.model.type_params

import org.kethereum.contract.abi.types.model.ETHTypeParams

data class BitsTypeParams(val bits: Int) : ETHTypeParams {
    override fun encodeToString() = bits.toString()

    companion object {
        fun decodeFromString(input: String) = BitsTypeParams(input.toInt())
    }
}