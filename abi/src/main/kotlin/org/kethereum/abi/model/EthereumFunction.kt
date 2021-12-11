package org.kethereum.abi.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EthereumFunction(
        val name: String,
        val payable: Boolean,
        val stateMutability: StateMutability,
        val inputs: List<EthereumNamedType>,
        val outputs: List<EthereumNamedType>
)