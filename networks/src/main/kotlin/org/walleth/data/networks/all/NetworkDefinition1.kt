package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition1 : NetworkDefinition {

    override val chain = ChainDefinition(ChainId(1L), "ETH")

    override fun getNetworkName() = "main"

    override val infoUrl = "https://ethstats.net"

    override val faucets = emptyList<String>()
}