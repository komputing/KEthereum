package org.kethereum.rpc.min3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class TheIN3NodeListParser {

    @Test
    fun parsesJSON() {
        val json = javaClass.getResource("/nodeList_response.json").readText()
        val nodeListResponse = in3nodeListResponseAdapter.fromJson(json)

        assertNotNull(nodeListResponse)
        assertThat(nodeListResponse.jsonrpc).isEqualTo("2.0")
        assertThat(nodeListResponse.in3.execTime).isEqualTo(0)
        assertThat(nodeListResponse.in3.lastValidatorChange).isEqualTo(0)
        assertThat(nodeListResponse.id).isEqualTo(1)
        assertThat(nodeListResponse.result.nodes.size).isEqualTo(22)

        val firstNode = nodeListResponse.result.nodes.first()

        assertThat(firstNode.url).isEqualTo("https://in3-v2.slock.it/mainnet/nd-1")
        assertThat(firstNode.address).isEqualTo("0x45d45e6ff99e6c34a235d263965910298985fcfe")
        assertThat(firstNode.index).isEqualTo(0)
        assertThat(firstNode.deposit).isEqualTo("0xe043da617250000")
        assertThat(firstNode.props).isEqualTo("0x6000001dd")
        assertThat(firstNode.timeout).isEqualTo(3456000)
        assertThat(firstNode.registerTime).isEqualTo(1576224418)
        assertThat(firstNode.weight).isEqualTo(2000)
    }

}