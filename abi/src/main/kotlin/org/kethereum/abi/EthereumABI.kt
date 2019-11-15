package org.kethereum.abi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.kethereum.abi.model.EthereumABIElement
import org.kethereum.abi.model.EthereumFunction

private fun parse(abi: String, moshi: Moshi) = createAdapter(moshi).fromJson(abi) ?: emptyList()

private fun createAdapter(moshi: Moshi): JsonAdapter<List<EthereumABIElement>> {
    val listMyData = Types.newParameterizedType(List::class.java, EthereumABIElement::class.java)
    return moshi.adapter(listMyData)
}

class EthereumABI(val methodList: List<EthereumABIElement>) {
    constructor(abiString: String, moshi: Moshi = Moshi.Builder().build()) : this(parse(abiString, moshi))
}

fun EthereumABI.toFunctionSignaturesString() = methodList.filter { it.name != null }.joinToString("\n") { function ->
    function.name + "(" + function.inputs?.joinToString(",") { it.type + " " + it.name } + ")"
}

fun Iterable<EthereumABIElement>.getAllFunctions() = filter { it.type == "function" }.map {
    EthereumFunction(
            name = it.name ?: throw IllegalArgumentException("A function MUST have a name"),
            inputs = it.inputs ?: emptyList(),
            outputs = it.outputs ?: emptyList()
    )
}