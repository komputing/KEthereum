package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.ETHType

fun encodeTypes(vararg types: ETHType<*>): ByteArray {
    var result = ByteArray(0)
    types.forEach {
        result += it.paddedValue
    }
    return result
}