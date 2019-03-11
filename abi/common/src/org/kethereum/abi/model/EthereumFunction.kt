package org.kethereum.abi.model

import kotlinx.serialization.Serializable

@Serializable
data class EthereumFunctionParameter(
        val name: String,
        val type: String
)

@Serializable
data class EthereumFunction(
        val name: String?,
        val id: String,
        val constant: Boolean,
        val inputs: List<EthereumFunctionParameter>?,
        val result: String
)