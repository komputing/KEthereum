package org.kethereum.erc831

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheERC831ExtensionFun {

    @Test
    fun canDetectEthereumURL() {
        assertThat("ethereum:0x00AB42".isEthereumURLString()).isTrue()
    }

    @Test
    fun canRejectNonEtherumURL() {
        assertThat("other:0x00AB42".isEthereumURLString()).isFalse()
    }

}