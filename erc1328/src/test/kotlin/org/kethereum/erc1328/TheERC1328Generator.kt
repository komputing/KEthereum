package org.kethereum.erc1328

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheERC1328Generator {

    @Test
    fun basicToERC1328Works() {
        assertThat(ERC1328(sessionID = "mySession").generateURL())
                .isEqualTo("ethereum:wc-mySession")
    }

    @Test
    fun thatERC1328WithVersionWorks() {
        assertThat(ERC1328(sessionID = "mySession", version = 2).generateURL())
                .isEqualTo("ethereum:wc-mySession@2")
    }

    @Test
    fun thatERC1328FullyFeaturedWorks() {
        assertThat(ERC1328(
                sessionID = "mySession",
                name = "myApp",
                bridge = "myBridge",
                symKey = "mySymKey",
                version = 2
        ).generateURL())
                .isEqualTo("ethereum:wc-mySession@2?name=myApp&bridge=myBridge&symKey=mySymKey")
    }

}
