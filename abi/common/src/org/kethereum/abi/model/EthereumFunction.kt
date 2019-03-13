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
        @Optional val constant: Boolean = false,
        @Optional val inputs: List<EthereumFunctionParameter>? = null,
        @Optional val name: String? = null,
        @Optional val id: String? = null,
        @Optional val result: String? = null
)