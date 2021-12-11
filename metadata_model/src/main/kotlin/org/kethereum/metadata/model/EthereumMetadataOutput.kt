package org.kethereum.metadata.model

import com.squareup.moshi.JsonClass
import org.kethereum.abi.model.EthereumABIElement

@JsonClass(generateAdapter = true)
data class EthereumMetadataOutput(
        val abi: List<EthereumABIElement>,
        val devdoc: Docs,
        val userdoc: Docs
)