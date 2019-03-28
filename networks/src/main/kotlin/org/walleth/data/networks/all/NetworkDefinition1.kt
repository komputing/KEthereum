package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition1 : NetworkDefinition {

    override val tokenShortName = "ETH"

    override val tokenName = "Ether"

    override val chain = ChainDefinition(ChainId(1L), tokenShortName)

    override fun getNetworkName() = "main"

    override val infoUrl = "https://ethstats.net"

    override val faucets = emptyList<String>()

    override val rpcEndpoints = listOf("https://mainnet.infura.io/v3/\${INFURA_API_KEY}", "https://api.mycryptoapi.com/eth")
}