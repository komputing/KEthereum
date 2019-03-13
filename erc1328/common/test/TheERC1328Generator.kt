package org.kethereum.erc1328

import kotlin.test.Test
import kotlin.test.assertEquals

class TheERC1328Generator {

    @Test
    fun basicToERC1328Works() {
        assertEquals(ERC1328(topic = "myTopic").generateURL(), "wc:myTopic")
    }

    @Test
    fun thatERC1328WithVersionWorks() {
        assertEquals(ERC1328(topic = "myTopic", version = 2).generateURL(), "wc:myTopic@2")
    }

    @Test
    fun thatERC1328FullyFeaturedWorks() {
        assertEquals(
            ERC1328(
                topic = "myTopic",
                bridge = "myBridge",
                symKey = "mySymKey",
                version = 2
            ).generateURL(),
            "wc:myTopic@2?bridge=myBridge&key=mySymKey"
        )
    }

}
