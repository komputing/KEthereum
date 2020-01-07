package org.kethereum.abi

import org.kethereum.abi.model.EthereumABIElement
import org.kethereum.abi.model.EthereumFunction

fun Iterable<EthereumABIElement>.getAllFunctions() = filter { it.type == "function" }.map {
    EthereumFunction(
            name = it.name ?: throw IllegalArgumentException("A function MUST have a name"),
            inputs = it.inputs ?: emptyList(),
            outputs = it.outputs ?: emptyList()
    )
}