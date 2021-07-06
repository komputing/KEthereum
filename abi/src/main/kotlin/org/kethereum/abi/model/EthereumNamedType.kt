package org.kethereum.abi.model

data class EthereumNamedType(
    val name: String,
    val type: String,
    val internalType: String? = null,
    val components: List<EthereumNamedType>? = null
)