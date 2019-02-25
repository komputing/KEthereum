package org.walleth.kethereum.etherscan

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId

class TheEtherScanBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun rinkebyAddressWorks() {
        assertThat(getEtherScanBlockExplorer(ChainDefinition(ChainId(4L), "RIN")).getAddressURL(address))
                .isEqualTo("https://rinkeby.etherscan.io/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun mainAddressWorks() {
        assertThat(getEtherScanBlockExplorer(ChainDefinition(ChainId(1L), "ETH")).getAddressURL(address))
                .isEqualTo("https://etherscan.io/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun baseAPIWorks() {
        assertThat(getEtherScanAPIBaseURL(ChainDefinition(ChainId(1L), "ETH")))
                .isEqualTo("https://api.etherscan.io")
    }

    @Test
    fun baseAPIRopstenWorks() {
        assertThat(getEtherScanAPIBaseURL(ChainDefinition(ChainId(3L), "ROP")))
                .isEqualTo("https://api-ropsten.etherscan.io")
    }


    @Test
    fun support4Networks() {
        assertThat(ALL_ETHERSCAN_SUPPORTED_NETWORKS.size)
                .isEqualTo(5)
    }


}
