package org.kethereum.abi

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.kethereum.erc681.toERC681
import org.kethereum.metadata.model.ResolvedUserDocResult
import org.kethereum.metadata.resolveFunctionUserDoc
import org.kethereum.model.ChainId
import org.kethereum.model.EthereumURI

class TheUserdocResolver {

    private val server = MockWebServer()

    @Before
    fun runBeforeEveryTest() {
        server.start()
    }

    @After
    fun runAfterEveryTest() {
        server.shutdown()
    }

    @Test
    fun canLoadFor681() {
        runBlocking {
            server.enqueue(MockResponse().setBody(SampleContractResponse))

            val tested1 = EthereumURI("ethereum:0x42f944FB203bfC72c13CF6467D57B8110E146B3F@5/mint?address=0x8e23ee67d1332ad560396262c48ffbb01f93d052&uint256=2").toERC681().resolveFunctionUserDoc(ChainId(5L),baseURL = listOf(server.url("").toString()))
            assertThat(tested1).isInstanceOf(ResolvedUserDocResult::class.java)
            assertThat((tested1 as ResolvedUserDocResult).userDoc).isEqualTo("Mints 2 tokens for 0x8e23ee67d1332ad560396262c48ffbb01f93d052")

            server.enqueue(MockResponse().setBody(SampleContractResponse))

            val tested2 = EthereumURI("ethereum:0x42f944FB203bfC72c13CF6467D57B8110E146B3F@5/mint?address=0x8e23ee67d1332ad560396262c48ffbb01f93d052&uint256=3").toERC681().resolveFunctionUserDoc(ChainId(5L),baseURL = listOf(server.url("").toString()))
            assertThat(tested2).isInstanceOf(ResolvedUserDocResult::class.java)
            assertThat((tested2 as ResolvedUserDocResult).userDoc).isEqualTo("Mints 3 tokens for 0x8e23ee67d1332ad560396262c48ffbb01f93d052")

        }
    }
}