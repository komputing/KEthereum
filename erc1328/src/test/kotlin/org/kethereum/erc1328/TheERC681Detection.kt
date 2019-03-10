package org.kethereum.erc1328

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.EthereumURI

class TheERC1328Detection {

    @Test
    fun detects1328URLWithPrefix() {
        assertThat(EthereumURI("wc:myTopic@1?name=foo&bridge?bar").isERC1328()).isTrue()
    }

    @Test
    fun detects1328URLWithPrefixSimple() {
        assertThat(EthereumURI("wc:myTopic").isERC1328()).isTrue()
    }

    @Test
    fun detectsNon1328URLForSomethingClose() {
        assertThat(EthereumURI("wccc:myTopic").isERC1328()).isFalse()
    }

    @Test
    fun detectsNon1328ForGarbage() {
        assertThat(EthereumURI("garbage").isERC1328()).isFalse()
    }


}