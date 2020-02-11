package org.walleth.kethereum.blockscout

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainId

class TheBlockScoutBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun mainAddressWorks() {
        assertThat(getBlockScoutBlockExplorer(ChainId(1))?.getAddressURL(address))
                .isEqualTo("https://blockscout.com/eth/mainnet/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun baseAPIWorks() {
        assertThat(getBlockscoutBaseURL(ChainId(1)))
                .isEqualTo("https://blockscout.com/eth/mainnet")
    }

    @Test
    fun baseAPIAthereumWorks() {
        assertThat(getBlockscoutBaseURL(ChainId(43110)))
                .isEqualTo("http://athexplorer.ava.network")
    }


    @Test
    fun support10Networks() {
        assertThat(ALL_BLOCKSCOUT_SUPPORTED_NETWORKS.size)
                .isEqualTo(7)
    }

    @Test
    fun returnsNullPathForBaseURLForUnknownNetwork() {
        assertThat(getBlockscoutBaseURL(ChainId(666)))
                .isEqualTo(null)
    }

    @Test
    fun returnsNullForBaseURLForUnknownNetwork() {
        assertThat(getBlockScoutBlockExplorer(ChainId(666)))
                .isEqualTo(null)
    }

}
