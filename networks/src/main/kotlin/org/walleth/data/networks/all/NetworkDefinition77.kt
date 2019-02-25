package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition77 : NetworkDefinition {

    override val tokenShortName = "SOKOL"
    override val tokenName = "Sokol (POA) Ether"

    override val chain = ChainDefinition(ChainId(77L), tokenShortName)

    override fun getNetworkName() = "POA Sokol"

    override val infoUrl = "https://poa.network"

    override val faucets = emptyList<String>()

    override val rpcEndpoints = listOf("https://sokol.poa.network")
}