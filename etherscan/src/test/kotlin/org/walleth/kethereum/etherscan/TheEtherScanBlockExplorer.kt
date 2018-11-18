package org.walleth.kethereum.etherscan

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition

class TheEtherScanBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun rinkebyAddressWorks() {
        assertThat(getEtherScanBlockExplorer(ChainDefinition(4L)).getAddressURL(address))
                .isEqualTo("https://rinkeby.etherscan.io/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun mainAddressWorks() {
        assertThat(getEtherScanBlockExplorer(ChainDefinition(1L)).getAddressURL(address))
                .isEqualTo("https://etherscan.io/address/0x1234567890123456789012345678901234567890")
    }

    @Test
    fun baseAPIWorks() {
        assertThat(getEtherScanAPIBaseURL(ChainDefinition(1L)))
                .isEqualTo("https://api.etherscan.io")
    }

    @Test
    fun baseAPIRopstenWorks() {
        assertThat(getEtherScanAPIBaseURL(ChainDefinition(3L)))
                .isEqualTo("https://api-ropsten.etherscan.io")
    }


    @Test
    fun support4Networks() {
        assertThat(ALL_ETHERSCAN_SUPPORTED_NETWORKS.size)
                .isEqualTo(4)
    }


}
