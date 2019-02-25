package org.walleth.kethereum.blockscout

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId

class TheBlockScoutBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun rinkebyAddressWorks() {
        assertThat(getBlockScoutBlockExplorer(ChainDefinition(ChainId(4L), "RIN")).getAddressURL(address))
                .isEqualTo("https://blockscout.com/eth/rinkeby/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun mainAddressWorks() {
        assertThat(getBlockScoutBlockExplorer(ChainDefinition(ChainId(1L), "ETH")).getAddressURL(address))
                .isEqualTo("https://blockscout.com/eth/mainnet/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun baseAPIWorks() {
        assertThat(getBlockscoutBaseURL(ChainDefinition(ChainId(1L), "ETH")))
                .isEqualTo("https://blockscout.com/eth/mainnet")
    }

    @Test
    fun baseAPIRopstenWorks() {
        assertThat(getBlockscoutBaseURL(ChainDefinition(ChainId(3L), "ROP")))
                .isEqualTo("https://blockscout.com/eth/ropsten")
    }


    @Test
    fun support9Networks() {
        assertThat(ALL_BLOCKSCOUT_SUPPORTED_NETWORKS.size)
                .isEqualTo(9)
    }


}
