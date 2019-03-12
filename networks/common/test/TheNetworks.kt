package org.kethereum.networks

import org.walleth.data.networks.ALL_NETWORKS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TheNetworks {

    @Test
    fun weHaveNetworks() {
        assertTrue(ALL_NETWORKS.isNotEmpty())
    }

    @Test
    fun chainIDsAreUnique() {
        val chainSet = ALL_NETWORKS.map { it.chain.id }.toSet()
        assertEquals(chainSet.size, ALL_NETWORKS.size)
    }

    @Test
    fun namesAreUnique() {
        val names = ALL_NETWORKS.map { it.getNetworkName() }.toSet()
        assertEquals(names.size, ALL_NETWORKS.size)
    }

}
