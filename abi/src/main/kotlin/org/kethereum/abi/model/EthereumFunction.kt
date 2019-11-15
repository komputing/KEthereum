package org.kethereum.abi.model

data class EthereumFunction(
        val name: String,
        val inputs: List<EthereumNamedType>,
        val outputs: List<EthereumNamedType>
)