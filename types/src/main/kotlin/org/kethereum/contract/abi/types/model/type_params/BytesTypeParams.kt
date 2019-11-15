package org.kethereum.contract.abi.types.model.type_params

import org.kethereum.contract.abi.types.model.ETHTypeParams

data class BytesTypeParams(val bytes: Int) : ETHTypeParams {
    override fun encodeToString() = bytes.toString()

    companion object {
        fun decodeFromString(input: String) = BytesTypeParams(input.toInt())
    }
}