package org.kethereum.methodsignatures.model

data class TextMethodSignature(val signature: String) {
    val functionName by lazy { signature.substringBefore("(").trim() }

    val parameters by lazy { signature.substringAfter("(").substringBefore(")").split(",").map { it.trim() } }

    // see https://solidity.readthedocs.io/en/develop/abi-spec.htm
    val normalizedParameters by lazy {
        parameters.map {
            when (it) {
                "uint" -> "uint256"
                "int" -> "int256"
                "fixed" -> "fixed128x18"
                "ufixed" -> "ufixed128x18"
                "byte" -> "bytes1"
                else -> it
            }
        }
    }

    private val normalizedParametersString by lazy { normalizedParameters.joinToString(",")}

    val normalizedSignature by lazy { "$functionName($normalizedParametersString)" }
}