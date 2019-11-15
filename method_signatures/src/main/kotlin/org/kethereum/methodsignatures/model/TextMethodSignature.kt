package org.kethereum.methodsignatures.model

import org.kethereum.contract.abi.types.model.TypeAliases


data class TextMethodSignature(val signature: String) {
    val functionName by lazy { signature.substringBefore("(").trim() }

    private val parameterElements by lazy {
        val params = signature.substringAfter("(").trim().removeSuffix(")")
        val paramList = mutableListOf<String>()
        var currentParam = ""

        params.toCharArray().forEach {
            when (it) {
                '(' -> {
                    paramList.add(currentParam)
                    currentParam = ""
                    paramList.add("(")
                }
                ')' -> {
                    paramList.add(currentParam)
                    currentParam = ""
                    paramList.add(")")
                }
                ',' -> {
                    paramList.add(currentParam)
                    currentParam = ""
                    paramList.add(",")
                }
                '[' -> {
                    paramList.add(currentParam)
                    currentParam = ""
                    paramList.add("[")
                }
                ']' -> {
                    paramList.add(currentParam)
                    currentParam = ""
                    paramList.add("]")
                }
                ' ', '\t' -> Unit
                else -> currentParam += it
            }
        }
        if (currentParam.isNotEmpty()) {
            paramList.add(currentParam)
        }
        paramList
    }

    // see https://solidity.readthedocs.io/en/develop/abi-spec.htm
    private val normalizedParameters by lazy {
        parameterElements.map {
            TypeAliases.getOrDefault(it, it)
        }
    }

    private val normalizedParametersString by lazy { normalizedParameters.joinToString("") }

    val normalizedSignature by lazy { "$functionName($normalizedParametersString)" }
}