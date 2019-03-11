package org.kethereum.abi.model

data class EthereumFunctionParameter(
        val name: String,
        val type: String
)

data class EthereumFunction(
        val name: String?,
        val id: String,
        val constant: Boolean,
        val inputs: List<EthereumFunctionParameter>?,
        val result: String
)