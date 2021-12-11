package org.kethereum.metadata.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EthereumSource(
        val keccak256: String,
        val urls: List<String>?
)