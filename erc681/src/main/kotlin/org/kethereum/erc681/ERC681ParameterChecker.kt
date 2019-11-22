package org.kethereum.erc681

import org.kethereum.contract.abi.types.convertStringToABITypeOrNull
import org.kethereum.contract.abi.types.getETHTypeInstanceOrNull


fun ERC681.findIllegalParamType() = functionParams.find { convertStringToABITypeOrNull(it.first) == null }

fun ERC681.findIllegalParamValue() = functionParams.find {
    try {
        getETHTypeInstanceOrNull(it.first, it.second)
    } catch (e: Exception) {
        null
    } == null
}
