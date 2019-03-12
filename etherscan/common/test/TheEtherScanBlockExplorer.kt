package org.walleth.kethereum.etherscan

import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import kotlin.test.Test
import kotlin.test.assertEquals

class TheEtherScanBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun rinkebyAddressWorks() {
        assertEquals(
            getEtherScanBlockExplorer(ChainDefinition(4L)).getAddressURL(address),
            "https://rinkeby.etherscan.io/address/0x1234567890123456789012345678901234567890"
        )
    }

    @Test
    fun mainAddressWorks() {
        assertEquals(
            getEtherScanBlockExplorer(ChainDefinition(1L)).getAddressURL(address),
            "https://etherscan.io/address/0x1234567890123456789012345678901234567890"
        )
    }

    @Test
    fun baseAPIWorks() {
        assertEquals(getEtherScanAPIBaseURL(ChainDefinition(1L)), "https://api.etherscan.io")
    }

    @Test
    fun baseAPIRopstenWorks() {
        assertEquals(getEtherScanAPIBaseURL(ChainDefinition(3L)), "https://api-ropsten.etherscan.io")
    }


    @Test
    fun support4Networks() {
        assertEquals(ALL_ETHERSCAN_SUPPORTED_NETWORKS.size, 5)
    }
}
