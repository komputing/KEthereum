package org.kethereum.abi

import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import org.kethereum.abi.model.EthereumFunction

private fun parse(abi: String): List<EthereumFunction> {
    // We use .nonstrict to avoid errors with unknown keys or others
    return Json.nonstrict.parse(EthereumFunction.serializer().list, abi)
}

class EthereumABI(val methodList: List<EthereumFunction>) {
    constructor(abiString: String) : this(parse(abiString))
}

fun EthereumABI.toFunctionSignaturesString() = methodList.filter { it.name != null }.joinToString("\n") { function ->
    function.name + "(" + function.inputs?.joinToString(",") { it.type + " " + it.name } + ")"
}