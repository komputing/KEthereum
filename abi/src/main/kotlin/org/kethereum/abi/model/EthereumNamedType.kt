package org.kethereum.abi.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EthereumNamedType(
    val name: String,
    val type: String,
    val internalType: String? = null,
    val components: List<EthereumNamedType>? = null
)