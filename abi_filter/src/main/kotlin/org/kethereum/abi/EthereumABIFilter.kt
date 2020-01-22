package org.kethereum.abi

import org.kethereum.abi.model.EthereumABIElement
import org.kethereum.abi.model.EthereumFunction
import org.kethereum.methodsignatures.model.HexMethodSignature
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.methodsignatures.toTextMethodSignature

fun List<EthereumFunction>.findByTextMethodSignature(signature: TextMethodSignature) =
        firstOrNull { it.toTextMethodSignature() == signature }

fun List<EthereumFunction>.findByHexMethodSignature(signature: HexMethodSignature) =
        firstOrNull { it.toTextMethodSignature().toHexSignature() == signature }

fun Iterable<EthereumABIElement>.getAllFunctions(): List<EthereumFunction> = filter { it.type == "function" }.map {
    EthereumFunction(
            name = it.name ?: throw IllegalArgumentException("A function MUST have a name"),
            inputs = it.inputs ?: emptyList(),
            outputs = it.outputs ?: emptyList()
    )
}