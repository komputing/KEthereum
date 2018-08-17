package org.kethereum.networks

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.walleth.data.networks.ALL_NETWORKS

class TheNetworks {

    @Test
    fun weHaveNetworks() {
        assertThat(ALL_NETWORKS).isNotEmpty
    }

    @Test
    fun chainIDsAreUnique() {
        val chainSet = ALL_NETWORKS.map { it.chain.id }.toSet()
        assertThat(chainSet.size).isEqualTo(ALL_NETWORKS.size)
    }

    @Test
    fun namesAreUnique() {
        val names = ALL_NETWORKS.map { it.getNetworkName() }.toSet()
        assertThat(names.size).isEqualTo(ALL_NETWORKS.size)
    }

}
