package org.kethereum.methodsignatures

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.model.createEmptyTransaction
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.nio.file.Files


class TheCachedOnlineMethodSignatureRepositoryImpl {

    val storeDir = Files.createTempDirectory("sigtmp").toFile()

    @AfterEach
    internal fun cleanup() {
        storeDir.deleteRecursively()
    }

    @Test
    fun worksForOneSignature() {
        val mockWebServer = MockWebServer()
        val tested = CachedOnlineMethodSignatureRepositoryImpl(OkHttpClient().newBuilder().build(),
                storeDir,
                mockWebServer.url("/signatures/").toString()
        )

        mockWebServer.enqueue(MockResponse().setBody("goodResult()"))

        val tx = createEmptyTransaction().copy(input = HexString("06fdde03").hexToByteArray())
        val signature = tested.getSignaturesFor(tx)
        assertThat(signature).containsExactly(TextMethodSignature("goodResult()"))

        assertThat(mockWebServer.takeRequest().path).isEqualTo("/signatures/06fdde03")
    }


    @Test
    fun worksForTwoSignatures() {
        val mockWebServer = MockWebServer()
        val tested = CachedOnlineMethodSignatureRepositoryImpl(OkHttpClient().newBuilder().build(),
                storeDir,
                mockWebServer.url("/signatures/").toString()
        )

        mockWebServer.enqueue(MockResponse().setBody("goodResult();anotherResult()"))

        val tx = createEmptyTransaction().copy(input = HexString("313ce567").hexToByteArray())
        val signature = tested.getSignaturesFor(tx)

        assertThat(signature).containsExactlyInAnyOrder(TextMethodSignature("goodResult()"), TextMethodSignature("anotherResult()"))

        assertThat(mockWebServer.takeRequest().path).isEqualTo("/signatures/313ce567")
    }



    @Test
    fun returnsEmptyForNoCallTx() {
        val mockWebServer = MockWebServer()
        val tested = CachedOnlineMethodSignatureRepositoryImpl(OkHttpClient().newBuilder().build(),
                storeDir,
                mockWebServer.url("/signatures/").toString()
        )

        mockWebServer.enqueue(MockResponse().setBody("goodResult();anotherResult()"))

        assertThat(tested.getSignaturesFor(createEmptyTransaction())).isEmpty()

    }
}
