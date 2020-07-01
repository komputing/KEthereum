package org.kethereum.metadata.model

data class EthereumMetaData(
        val version: Int,
        val compiler: EthereumCompiler,
        val language: String,
        val output: EthereumMetadataOutput,
        val sources: Map<String, EthereumSource>,
        val settings: Settings
)