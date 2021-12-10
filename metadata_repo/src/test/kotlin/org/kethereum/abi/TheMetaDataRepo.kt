package org.kethereum.abi

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.TEST_ADDRESSES
import org.kethereum.metadata.repo.MetaDataRepoHttpWithCacheImpl
import org.kethereum.metadata.repo.model.MetaDataNotAvailable
import org.kethereum.metadata.repo.model.MetaDataResolveFail
import org.kethereum.metadata.repo.model.MetaDataResolveResultOK
import org.kethereum.metadata.repo.model.getMetaDataForTransaction
import org.kethereum.model.ChainId
import org.kethereum.model.createEmptyTransaction
import java.io.File
import java.math.BigInteger.valueOf

class TheMetaDataRepo {

    @Test
    fun returnsFailForNoRepos() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val tested = MetaDataRepoHttpWithCacheImpl(repoURL = mockWebServer.url("").toString())
        assertThat(tested.getMetaDataForAddressOnChain(TEST_ADDRESSES.first(), ChainId(5))).isInstanceOf(MetaDataNotAvailable::class.java)
    }

    @Test
    fun canReadFromCache() {

        val cache = createTempDir()
        File(cache, TEST_ADDRESSES.first().toString() + 5).apply {
            createNewFile()
            writer().use { writeText(testMetaDataJSON) }
        }
        val tested = MetaDataRepoHttpWithCacheImpl(cacheDir = cache)

        assertThat(tested.getMetaDataForAddressOnChain(TEST_ADDRESSES.first(), ChainId(5))).isInstanceOf(MetaDataResolveResultOK::class.java)
    }


    @Test
    fun returnsNotAvailableWithCacheButWrongContent() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val cache = createTempDir()
        File(cache, TEST_ADDRESSES.last().toString() + 5).apply {
            createNewFile()
            writer().use { writeText(testMetaDataJSON) }
        }
        val tested = MetaDataRepoHttpWithCacheImpl(cacheDir = cache, repoURL = mockWebServer.url("").toString())

        assertThat(tested.getMetaDataForAddressOnChain(TEST_ADDRESSES.first(), ChainId(5))).isInstanceOf(MetaDataNotAvailable::class.java)
    }

    @Test
    fun returnsDataFromWebServer() {
        val mockWebServer = MockWebServer()

        val tested = MetaDataRepoHttpWithCacheImpl(repoURL = mockWebServer.url("").toString())
        mockWebServer.enqueue(MockResponse().setBody(testMetaDataJSON))

        assertThat(tested.getMetaDataForAddressOnChain(TEST_ADDRESSES.first(), ChainId(5))).isInstanceOf(MetaDataResolveResultOK::class.java)
    }


    @Test
    fun returnsNotAvailableFor404() {
        val mockWebServer = MockWebServer()

        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val tested = MetaDataRepoHttpWithCacheImpl(repoURL = mockWebServer.url("").toString())

        assertThat(tested.getMetaDataForAddressOnChain(TEST_ADDRESSES.first(), ChainId(5))).isInstanceOf(MetaDataNotAvailable::class.java)
    }

    @Test
    fun returnsErrorFor500() {
        val mockWebServer = MockWebServer()

        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        val tested = MetaDataRepoHttpWithCacheImpl(repoURL = mockWebServer.url("").toString())

        assertThat(tested.getMetaDataForAddressOnChain(TEST_ADDRESSES.first(), ChainId(5))).isInstanceOf(MetaDataResolveFail::class.java)
    }


    @Test
    fun returnsFailForBadTransaction() {
        val mockWebServer = MockWebServer()

        val tested = MetaDataRepoHttpWithCacheImpl(repoURL = mockWebServer.url("").toString())
        mockWebServer.enqueue(MockResponse().setBody(testMetaDataJSON))

        assertThat(tested.getMetaDataForTransaction(createEmptyTransaction())).isInstanceOf(MetaDataResolveFail::class.java)
    }

    @Test
    fun returnsOKForGoodTransaction() {
        val mockWebServer = MockWebServer()

        val tested = MetaDataRepoHttpWithCacheImpl(repoURL = mockWebServer.url("").toString())
        mockWebServer.enqueue(MockResponse().setBody(testMetaDataJSON))

        val tx = createEmptyTransaction().copy(to = TEST_ADDRESSES.first(), chain = valueOf(5L))
        assertThat(tested.getMetaDataForTransaction(tx)).isInstanceOf(MetaDataResolveResultOK::class.java)
    }


}