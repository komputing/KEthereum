package org.kethereum.rpc

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.Address
import org.komputing.khex.model.HexString

class TheMultiURLEthereumTransport {

    private val server = MockWebServer()
    private val server1URL = server.url("1").toString()
    private val server2URL = server.url("2").toString()
    private val tested by lazy { MultiHostHttpTransport(urls = listOf(server1URL,server2URL)) }

    @Before
    fun runBeforeEveryTest() {
        server.start()
    }

    @After
    fun runAfterEveryTest() {
        server.shutdown()
    }

    @Test
    fun doesNotChangeServerWhenGettingResponses() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0x0234c8a3397aab58\"}\n"
        server.enqueue(MockResponse().setBody(response))

        assertThat(tested.call("foo")).isEqualTo(response)

        assertThat(server.takeRequest().requestUrl.toString()).isEqualTo(server1URL)


        server.enqueue(MockResponse().setBody(response))
        assertThat(tested.call("foo")).isEqualTo(response)
        assertThat(server.takeRequest().requestUrl.toString()).isEqualTo(server1URL)
    }

    @Test
    fun changesServerWhenBadResponse() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0x0234c8a3397aab58\"}\n"

        server.enqueue(MockResponse().setResponseCode(500))
        assertThat(tested.call("foo")).isEqualTo(null)
        assertThat(server.takeRequest().requestUrl.toString()).isEqualTo(server1URL)


        server.enqueue(MockResponse().setBody(response))
        assertThat(tested.call("foo")).isEqualTo(response)
        assertThat(server.takeRequest().requestUrl.toString()).isEqualTo(server2URL)

        server.enqueue(MockResponse().setBody(response))
        assertThat(tested.call("foo")).isEqualTo(response)
        assertThat(server.takeRequest().requestUrl.toString()).isEqualTo(server2URL)
    }

}