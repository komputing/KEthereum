package org.kethereum.erc831

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheERC831ExtensionFun {

    @Test
    fun canDetectEthereumURL() {
        assertTrue("ethereum:0x00AB42".isEthereumURLString())
    }

    @Test
    fun canRejectNonEtherumURL() {
        assertFalse("other:0x00AB42".isEthereumURLString())
    }
}