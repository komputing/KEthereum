package org.walleth.data.networks

import org.kethereum.model.ChainDefinition

interface NetworkDefinition {
    fun getNetworkName(): String

    val chain: ChainDefinition
    val infoUrl: String
    val faucets: List<String>
}