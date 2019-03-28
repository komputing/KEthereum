package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition3 : NetworkDefinition {

    override val tokenShortName = "ROP"
    override val tokenName = "Ropsten Ether"

    override val chain = ChainDefinition(ChainId(3L), tokenShortName)

    override fun getNetworkName() = "ropsten"

    override val infoUrl = "https://github.com/ethereum/ropsten"

    override val faucets = listOf(
            "https://faucet.ropsten.be?%address%"
    )

    override val rpcEndpoints = listOf("https://ropsten.infura.io/v3/\${INFURA_API_KEY}")
}