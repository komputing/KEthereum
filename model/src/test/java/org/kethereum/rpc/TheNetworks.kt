package org.kethereum.rpc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.ETHEREUM_NETWORKS

class TheNetworks {

    @Test
    fun weHaveNetworks() {
        assertThat(ETHEREUM_NETWORKS).isNotEmpty
    }
}