package org.kethereum.erc1328

import org.kethereum.model.EthereumURI
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheERC1328Detection {

    @Test
    fun detects1328URLWithPrefix() {
        assertTrue(EthereumURI("wc:myTopic@1?name=foo&bridge?bar").isERC1328())
    }

    @Test
    fun detects1328URLWithPrefixSimple() {
        assertTrue(EthereumURI("wc:myTopic").isERC1328())
    }

    @Test
    fun detectsNon1328URLForSomethingClose() {
        assertFalse(EthereumURI("wccc:myTopic").isERC1328())
    }

    @Test
    fun detectsNon1328ForGarbage() {
        assertFalse(EthereumURI("garbage").isERC1328())
    }
}