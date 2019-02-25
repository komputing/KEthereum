package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition99 : NetworkDefinition {

    override val tokenShortName = "POA"
    override val tokenName = "POA Ether"

    override val chain = ChainDefinition(ChainId(99L), tokenShortName)

    override fun getNetworkName() = "POA"

    override val infoUrl = "https://poa.network"

    override val faucets = emptyList<String>()

    override val rpcEndpoints = listOf("https://core.poa.network")
}