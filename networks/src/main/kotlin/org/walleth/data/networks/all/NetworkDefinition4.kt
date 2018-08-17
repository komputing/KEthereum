package org.walleth.data.networks.all

import org.kethereum.model.ChainDefinition
import org.walleth.data.networks.NetworkDefinition


class NetworkDefinition4 : NetworkDefinition {

    override val chain = ChainDefinition(4L, "RIN")

    override fun getNetworkName() = "rinkeby"

    override val infoUrl = "https://www.rinkeby.io"

    override val faucets = listOf(
            "https://faucet.rinkeby.io"
    )

}