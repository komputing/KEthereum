package org.kethereum.abi.model

enum class StateMutability {
    pure,
    view,
    payable,
    nonpayable
}

data class EthereumABIElement(
        val name: String?,
        val id: String?,
        val constant: Boolean?,
        val inputs: List<EthereumNamedType>?,
        val outputs: List<EthereumNamedType>?,
        val result: String?,
        val payable: Boolean?,
        val stateMutability: StateMutability?,
        val type: String
)