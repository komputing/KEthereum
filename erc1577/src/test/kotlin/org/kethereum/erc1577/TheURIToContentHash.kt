package org.kethereum.erc1577

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.kethereum.erc1577.model.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.net.URI
import kotlin.test.fail

class TheURIToContentHash {

    companion object {
        @JvmStatic
        fun provideTestVectors() = testVectors
    }

    @ParameterizedTest
    @MethodSource("provideTestVectors")
    fun `can handle test vectors`(contentHash: String, uriString: String) {
        val uri = URI.create(uriString)

        val hex = HexString(contentHash)

        val contentHashResult = uri.toContentHash()

        if (contentHashResult !is SuccessfulToContentHashResult) {
            fail("result was not SuccessfulToContentHashResult")
        }

        assertThat(contentHashResult.contentHash.byteArray).isEqualTo(ContentHash(hex.hexToByteArray()).byteArray)

    }

    @Test
    fun `can handle invalid scheme`() {

        assertThat(URI.create("yolo://shouldfail").toContentHash()).isInstanceOf(InvalidSchemeToURIError::class.java)
    }

    @Test
    fun `can handle invalid bzz`() {

        assertThat(URI.create("bzz://yolo").toContentHash()).isInstanceOf(InvalidSwarmURL::class.java)
    }
}
