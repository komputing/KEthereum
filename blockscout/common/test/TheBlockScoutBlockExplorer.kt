package org.walleth.kethereum.blockscout

import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import kotlin.test.Test
import kotlin.test.assertEquals

class TheBlockScoutBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun rinkebyAddressWorks() {
        assertEquals(
            getBlockScoutBlockExplorer(ChainDefinition(ChainId(4L), "RIN")).getAddressURL(address),
            "https://blockscout.com/eth/rinkeby/address/0x1234567890123456789012345678901234567890"
        )
    }

    @Test
    fun mainAddressWorks() {
        assertEquals(getBlockScoutBlockExplorer(
            ChainDefinition(ChainId(1L), "ETH")).getAddressURL(address),
            "https://blockscout.com/eth/mainnet/address/0x1234567890123456789012345678901234567890"
        )
    }

    @Test
    fun baseAPIWorks() {
        assertEquals(
            getBlockscoutBaseURL(ChainDefinition(ChainId(1L), "ETH")),
            "https://blockscout.com/eth/mainnet"
        )
    }

    @Test
    fun baseAPIRopstenWorks() {
        assertEquals(getBlockscoutBaseURL(
            ChainDefinition(ChainId(3L), "ROP")),
            "https://blockscout.com/eth/ropsten"
        )
    }


    @Test
    fun support9Networks() {
        assertEquals(ALL_BLOCKSCOUT_SUPPORTED_NETWORKS.size, 9)
    }


}
