package org.walleth.data.networks

import org.kethereum.model.ChainDefinition

interface NetworkDefinition {
    fun getNetworkName(): String

    val tokenName: String
    val tokenShortName: String

    val chain: ChainDefinition
    val infoUrl: String
    val faucets: List<String>

    val rpcEndpoints: List<String>
}