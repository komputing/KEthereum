package org.kethereum.erc1328

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheERC1328Generator {

    @Test
    fun basicToERC1328Works() {
        assertThat(ERC1328(topic = "myTopic").generateURL())
                .isEqualTo("wc:myTopic")
    }

    @Test
    fun thatERC1328WithVersionWorks() {
        assertThat(ERC1328(topic = "myTopic", version = 2).generateURL())
                .isEqualTo("wc:myTopic@2")
    }

    @Test
    fun thatERC1328FullyFeaturedWorks() {
        assertThat(ERC1328(
                topic = "myTopic",
                bridge = "myBridge",
                symKey = "mySymKey",
                version = 2
        ).generateURL())
                .isEqualTo("wc:myTopic@2?bridge=myBridge&key=mySymKey")
    }

}
