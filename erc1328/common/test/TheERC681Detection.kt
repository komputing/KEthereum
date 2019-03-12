package org.kethereum.erc1328

import org.kethereum.model.EthereumURI
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheERC1328Detection {

    @Test
    fun detects1328URLWithPrefix() {
        assertTrue(EthereumURI("ethereum:wc-mySession@1?name=foo&bridge?bar").isERC1328())
    }

    @Test
    fun detects1328URLWithPrefixSimple() {
        assertTrue(EthereumURI("ethereum:wc-mySession").isERC1328())
    }

    @Test
    fun detectsNon1328URLNoPrefixSimple() {
        assertFalse(EthereumURI("ethereum:mySession").isERC1328())
    }


    @Test
    fun detectsNon1328URLOtherPrefix() {
        assertFalse(EthereumURI("ethereum:somethingelse-0x00AB42@23?value=42&gas=3&yay").isERC1328())
    }


    @Test
    fun detectsNon1328URLForSomethingClose() {
        assertFalse(EthereumURI("ethereum:wccc-mySession").isERC1328())
    }

    @Test
    fun detectsNon1328ForGarbage() {
        assertFalse(EthereumURI("garbage").isERC1328())
    }
}