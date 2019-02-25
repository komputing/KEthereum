package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import org.walleth.data.networks.NetworkDefinition


class NetworkDefinition5 : NetworkDefinition {

    override val tokenShortName = "GOR"
    override val tokenName = "Goerli Ether"

    override val chain = ChainDefinition(ChainId(5L), tokenShortName)

    override fun getNetworkName() = "goerli"

    override val infoUrl = "https://goerli.net/#about"

    override val faucets = listOf(
            "https://goerli-faucet.slock.it/?address=%address%"
    )

    override val rpcEndpoints = listOf("https://rpc.goerli.mudit.blog/",
            "https://rpc.slock.it/goerli ",
            "https://goerli.prylabs.net/"
    )

}