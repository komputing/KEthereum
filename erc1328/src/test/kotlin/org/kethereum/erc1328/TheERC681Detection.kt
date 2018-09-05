package org.kethereum.erc1328

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.model.EthereumURI

class TheERC1328Detection {

    @Test
    fun detects1328URLWithPrefix() {
        assertThat(EthereumURI("ethereum:wc-mySession@1?name=foo&bridge?bar").isERC1328()).isTrue()
    }

    @Test
    fun detects1328URLWithPrefixSimple() {
        assertThat(EthereumURI("ethereum:wc-mySession").isERC1328()).isTrue()
    }

    @Test
    fun detectsNon1328URLNoPrefixSimple() {
        assertThat(EthereumURI("ethereum:mySession").isERC1328()).isFalse()
    }


    @Test
    fun detectsNon1328URLOtherPrefix() {
        assertThat(EthereumURI("ethereum:somethingelse-0x00AB42@23?value=42&gas=3&yay").isERC1328()).isFalse()
    }


    @Test
    fun detectsNon1328URLForSomethingClose() {
        assertThat(EthereumURI("ethereum:wccc-mySession").isERC1328()).isFalse()
    }

    @Test
    fun detectsNon1328ForGarbage() {
        assertThat(EthereumURI("garbage").isERC1328()).isFalse()
    }


}