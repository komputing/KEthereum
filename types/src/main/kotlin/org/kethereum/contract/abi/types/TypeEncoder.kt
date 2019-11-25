package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.types.UIntETHType
import java.math.BigInteger

fun encodeTypes(vararg types: ETHType<*>): ByteArray {
    val staticPart = mutableListOf<ByteArray>()
    val dynamicPart = mutableListOf<ByteArray>()
    var dynamicPos = BigInteger.valueOf(types.size * 32L)
    types.forEach {
        if (!it.isDynamic()) {
            staticPart.add(it.paddedValue)
        } else {
            staticPart.add(UIntETHType.ofNativeKotlinType(dynamicPos, BitsTypeParams(32)).paddedValue)
            dynamicPart.add(it.paddedValue)
            dynamicPos += BigInteger.valueOf(32L * it.paddedValue.size)
        }
    }
    var result = ByteArray(0)
    staticPart.forEach { result += it }
    dynamicPart.forEach { result += it }
    return result
}