package org.kethereum.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheNetworks {

    @Test
    fun weHaveNetworks() {
        assertThat(ETHEREUM_NETWORKS).isNotEmpty
    }
}