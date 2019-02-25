package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition

class NetworkDefinition42 : NetworkDefinition {

    override val tokenName = "KOV"

    override val chain = ChainDefinition(ChainId(42L), "KOV")

    override fun getNetworkName() = "kovan"

    override val infoUrl = "https://kovan-testnet.github.io/website"

    override val faucets = listOf(
            "https://faucet.kovan.network",
            "https://gitter.im/kovan-testnet/faucet"
    )

    override val rpcEndpoints = listOf("https://kovan.infura.io")

}