package org.kethereum.metadata.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EthereumMetaData(
        val version: Int,
        val compiler: EthereumCompiler,
        val language: String,
        val output: EthereumMetadataOutput,
        val sources: Map<String, EthereumSource>,
        val settings: Settings
)