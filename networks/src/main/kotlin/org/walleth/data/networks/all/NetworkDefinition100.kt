package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition100 : NetworkDefinition {

    override val tokenName = "xDAI"

    override val chain = ChainDefinition(ChainId(100L), tokenName)

    override fun getNetworkName() = "xDai"

    override val infoUrl = "https://forum.poa.network/c/xdai-chain"

    override val faucets = emptyList<String>()
}