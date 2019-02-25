package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition61 : NetworkDefinition {

    override val tokenShortName = "ETC"
    override val tokenName = "Ethereum Classic Ether"

    override val chain = ChainDefinition(ChainId(61L), tokenShortName)

    override fun getNetworkName() = "Ethereum Classic"

    override val infoUrl = "https://ethereumclassic.org"

    override val faucets = emptyList<String>()

    override val rpcEndpoints = listOf("https://ethereumclassic.network")
}