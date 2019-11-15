package org.kethereum.abi.model

data class EthereumABIElement(
        val name: String?,
        val id: String,
        val constant: Boolean,
        val inputs: List<EthereumNamedType>?,
        val outputs: List<EthereumNamedType>?,
        val result: String?,
        val type: String
)