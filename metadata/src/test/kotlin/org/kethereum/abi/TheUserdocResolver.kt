package org.kethereum.abi

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.TEST_ADDRESSES
import org.kethereum.erc681.toERC681
import org.kethereum.metadata.model.NoMatchingUserDocFound
import org.kethereum.metadata.model.ResolveErrorUserDocResult
import org.kethereum.metadata.model.ResolvedUserDocResult
import org.kethereum.metadata.repo.MetaDataRepoHttpWithCacheImpl
import org.kethereum.metadata.repo.model.MetaDataRepo
import org.kethereum.metadata.resolveFunctionUserDoc
import org.kethereum.model.ChainId
import org.kethereum.model.EthereumURI
import org.kethereum.model.createEmptyTransaction
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.math.BigInteger

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

            val tested1 = EthereumURI("ethereum:0x42f944FB203bfC72c13CF6467D57B8110E146B3F@5/mint?address=0x8e23ee67d1332ad560396262c48ffbb01f93d052&uint256=2").toERC681().resolveFunctionUserDoc(ChainId(5L),metaDataRepo = MetaDataRepoHttpWithCacheImpl(server.url("").toString()))
            assertThat(tested1).isInstanceOf(ResolvedUserDocResult::class.java)
            assertThat((tested1 as ResolvedUserDocResult).userDoc).isEqualTo("Mints 2 tokens for 0x8e23ee67d1332ad560396262c48ffbb01f93d052")

            server.enqueue(MockResponse().setBody(SampleContractResponse))

            val tested2 = EthereumURI("ethereum:0x42f944FB203bfC72c13CF6467D57B8110E146B3F@5/mint?address=0x8e23ee67d1332ad560396262c48ffbb01f93d052&uint256=3").toERC681().resolveFunctionUserDoc(ChainId(5L),metaDataRepo = MetaDataRepoHttpWithCacheImpl(server.url("").toString()))
            assertThat(tested2).isInstanceOf(ResolvedUserDocResult::class.java)
            assertThat((tested2 as ResolvedUserDocResult).userDoc).isEqualTo("Mints 3 tokens for 0x8e23ee67d1332ad560396262c48ffbb01f93d052")

        }
    }

    @Test
    fun canLoadForTransaction() {
        runBlocking {
            server.enqueue(MockResponse().setBody(SampleContractResponse))
            val tested = createEmptyTransaction().copy(
                    input = HexString("0x40c10f190000000000000000000000004ceaf85436b449d565f24d8a43b5c0efad50437a000000000000000000000000000000000000000000000016c4abbebea0100000").hexToByteArray(),
                    chain = BigInteger.ONE,
                    to = TEST_ADDRESSES.first()
            ).resolveFunctionUserDoc(metaDataRepo = MetaDataRepoHttpWithCacheImpl(server.url("").toString()))

            assertThat(tested).isInstanceOf(ResolvedUserDocResult::class.java)
            assertThat((tested as ResolvedUserDocResult).userDoc).isEqualTo("Mints 420000000000000000000 tokens for 0x4ceaf85436b449d565f24d8a43b5c0efad50437a")
        }
    }
    @Test
    fun returnsNullWhenServerRespondsWithNonJSON() {
        runBlocking {
            server.enqueue(MockResponse().setBody("yiki"))

            val tested1 = EthereumURI("ethereum:0x42f944FB203bfC72c13CF6467D57B8110E146B3F@5/mint?address=0x8e23ee67d1332ad560396262c48ffbb01f93d052&uint256=2").toERC681().resolveFunctionUserDoc(ChainId(5L),metaDataRepo = MetaDataRepoHttpWithCacheImpl(server.url("").toString()))
            assertThat(tested1).isInstanceOf(ResolveErrorUserDocResult::class.java)

        }
    }
}