package org.kethereum.erc1328

import kotlin.test.Test
import kotlin.test.assertEquals

class TheERC1328Generator {

    @Test
    fun basicToERC1328Works() {
        assertEquals(ERC1328(sessionID = "mySession").generateURL(), "ethereum:wc-mySession")
    }

    @Test
    fun thatERC1328WithVersionWorks() {
        assertEquals(ERC1328(sessionID = "mySession", version = 2).generateURL(), "ethereum:wc-mySession@2")
    }

    @Test
    fun thatERC1328FullyFeaturedWorks() {
        assertEquals(
            ERC1328(
                sessionID = "mySession",
                name = "myApp",
                bridge = "myBridge",
                symKey = "mySymKey",
                version = 2
            ).generateURL(),
            "ethereum:wc-mySession@2?name=myApp&bridge=myBridge&symKey=mySymKey"
        )
    }

}
