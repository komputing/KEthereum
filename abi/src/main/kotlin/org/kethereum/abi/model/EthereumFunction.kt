package org.kethereum.abi.model

data class EthereumFunction(
        val name: String,
        val payable: Boolean,
        val stateMutability: StateMutability,
        val inputs: List<EthereumNamedType>,
        val outputs: List<EthereumNamedType>
)