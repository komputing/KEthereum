package org.walleth.kethereum.etherscan

import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.ChainId
import kotlin.test.Test
import kotlin.test.assertEquals

class TheEtherScanBlockExplorer {

    private val address = Address("0x1234567890123456789012345678901234567890")

    @Test
    fun rinkebyAddressWorks() {
        assertEquals(
            getEtherScanBlockExplorer(ChainDefinition(ChainId(4L), "ETH")).getAddressURL(address),
            "https://rinkeby.etherscan.io/address/0x1234567890123456789012345678901234567890"
        )
    }

    @Test
    fun mainAddressWorks() {
        assertEquals(
            getEtherScanBlockExplorer(ChainDefinition(ChainId(1L), "ETH")).getAddressURL(address),
            "https://etherscan.io/address/0x1234567890123456789012345678901234567890"
        )
    }

    @Test
    fun baseAPIWorks() {
        assertEquals(getEtherScanAPIBaseURL(ChainDefinition(ChainId(1L), "ETH")), "https://api.etherscan.io")
    }

    @Test
    fun baseAPIRopstenWorks() {
        assertEquals(getEtherScanAPIBaseURL(ChainDefinition(ChainId(3L), "ETH")), "https://api-ropsten.etherscan.io")
    }


    @Test
    fun support4Networks() {
        assertEquals(ALL_ETHERSCAN_SUPPORTED_NETWORKS.size, 5)
    }
}
