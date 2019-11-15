package org.kethereum.abi_codegen

import org.kethereum.abi.model.EthereumFunction
import org.kethereum.abi_codegen.model.Function
import org.kethereum.abi_codegen.model.Output
import org.kethereum.abi_codegen.model.Params
import org.kethereum.contract.abi.types.convertStringToABITypeOrNull
import org.kethereum.methodsignatures.toTextMethodSignature

fun List<EthereumFunction>.createModel(): List<Function> {
    val nameCounter = countNameDuplicates()
    return map {
        var blankParamCounter = -1

        Function(
                functionName = it.name,
                textMethodSignature = it.toTextMethodSignature(),
                outputs = it.outputs.map { out -> Output(out.type) },
                params = it.inputs.map { input ->
                    Params(
                            type = input.type,
                            parameterName = if (input.name.isBlank()) {
                                blankParamCounter++
                                "parameter$blankParamCounter"
                            } else {
                                input.name
                            },
                            typeDefinition = convertStringToABITypeOrNull(input.type)
                    )
                },
                nameUsedMoreThanOnce = nameCounter[it.name]!! > 1
        )
    }
}

private fun List<EthereumFunction>.countNameDuplicates() = mutableMapOf<String, Int>().apply {
    this@countNameDuplicates.forEach {
        this[it.name] = getOrDefault(it.name, 0) + 1
    }
}
