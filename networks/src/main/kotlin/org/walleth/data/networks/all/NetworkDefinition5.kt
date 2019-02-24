package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition


class NetworkDefinition5 : NetworkDefinition {

    override val chain = ChainDefinition(ChainId(5L), "GOR")

    override fun getNetworkName() = "goerli"

    override val infoUrl = "https://goerli.net/#about"

    override val faucets = listOf(
            "https://goerli-faucet.slock.it/?address=%address%"
    )

}