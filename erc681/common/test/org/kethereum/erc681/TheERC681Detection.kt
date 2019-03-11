package org.kethereum.erc681

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.EthereumURI

class TheERC681Detection {

    @Test
    fun detects681URLWithPrefix() {
        assertThat(EthereumURI("ethereum:pay-0x00AB42@23?value=42&gas=3&yay").isERC681()).isTrue()
    }

    @Test
    fun detects681URLWithPrefixSimple() {
        assertThat(EthereumURI("ethereum:pay-0x00AB42").isERC681()).isTrue()
    }

    @Test
    fun detects681URLNoPrefix() {
        assertThat(EthereumURI("ethereum:0x00AB42@23?value=42&gas=3&yay").isERC681()).isTrue()
    }

    @Test
    fun detects681URLNoPrefixSimple() {
        assertThat(EthereumURI("ethereum:0x00AB42").isERC681()).isTrue()
    }

    @Test
    fun detectsNon681URLNoPrefix() {
        assertThat(EthereumURI("ethereum:somethingelse-0x00AB42@23?value=42&gas=3&yay").isERC681()).isFalse()
    }


    @Test
    fun detectsNon681URLForSomethingClose() {
        assertThat(EthereumURI("ethereum:payy-0x00AB42@23?value=42&gas=3&yay").isERC681()).isFalse()
    }

    @Test
    fun detectsNon681ForGarbage() {
        assertThat(EthereumURI("garbage").isERC681()).isFalse()
    }

}