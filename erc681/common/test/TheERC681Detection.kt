package org.kethereum.erc681

import org.kethereum.model.EthereumURI
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheERC681Detection {

    @Test
    fun detects681URLWithPrefix() {
        assertTrue(EthereumURI("ethereum:pay-0x00AB42@23?value=42&gas=3&yay").isERC681())
    }

    @Test
    fun detects681URLWithPrefixSimple() {
        assertTrue(EthereumURI("ethereum:pay-0x00AB42").isERC681())
    }

    @Test
    fun detects681URLNoPrefix() {
        assertTrue(EthereumURI("ethereum:0x00AB42@23?value=42&gas=3&yay").isERC681())
    }

    @Test
    fun detects681URLNoPrefixSimple() {
        assertTrue(EthereumURI("ethereum:0x00AB42").isERC681())
    }

    @Test
    fun detectsNon681URLNoPrefix() {
        assertFalse(EthereumURI("ethereum:somethingelse-0x00AB42@23?value=42&gas=3&yay").isERC681())
    }


    @Test
    fun detectsNon681URLForSomethingClose() {
        assertFalse(EthereumURI("ethereum:payy-0x00AB42@23?value=42&gas=3&yay").isERC681())
    }

    @Test
    fun detectsNon681ForGarbage() {
        assertFalse(EthereumURI("garbage").isERC681())
    }

}