package org.kethereum.metadata.model

import org.kethereum.abi.model.EthereumABIElement

data class EthereumMetadataOutput(
        val abi: List<EthereumABIElement>,
        val devdoc: Docs,
        val userdoc: Docs
)