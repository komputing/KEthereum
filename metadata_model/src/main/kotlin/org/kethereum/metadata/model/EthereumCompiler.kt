package org.kethereum.metadata.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EthereumCompiler(val version: String)