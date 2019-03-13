package org.kethereum.abi.model

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class EthereumFunctionParameter(
        val name: String,
        val type: String
)

@Serializable
data class EthereumFunction(
        val constant: Boolean,
        val inputs: List<EthereumFunctionParameter>?,
        val name: String?,
        @Optional val id: String? = null,
        @Optional val result: String? = null
)