package org.kethereum.erc681

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheERC681ExtensionFun {

    @Test
    fun canDetectEthereumURL() {
        assertThat("ethereum:0x00AB42".isEthereumURLString()).isTrue()
    }

    @Test
    fun canRejectNonEtherumURL() {
        assertThat("other:0x00AB42".isEthereumURLString()).isFalse()
    }

}