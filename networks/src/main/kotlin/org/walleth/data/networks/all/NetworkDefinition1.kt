package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition1 : NetworkDefinition {

    override val tokenName = "ETH"

    override val chain = ChainDefinition(ChainId(1L), tokenName)

    override fun getNetworkName() = "main"

    override val infoUrl = "https://ethstats.net"

    override val faucets = emptyList<String>()

    override val rpcEndpoints = listOf("https://mainnet.infura.io", "https://api.mycryptoapi.com/eth")
}