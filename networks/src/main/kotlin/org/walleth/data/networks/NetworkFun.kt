package org.walleth.data.networks

import org.kethereum.model.ChainId

fun getNetworkDefinitionByChainID(chainID: ChainId) = ALL_NETWORKS.firstOrNull { it.chain.id == chainID }

fun ChainId.findNetworkDefinition() = ALL_NETWORKS.firstOrNull { it.chain.id == this }