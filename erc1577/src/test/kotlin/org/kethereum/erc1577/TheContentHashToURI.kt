package org.kethereum.erc1577

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.kethereum.erc1577.*
import org.kethereum.erc1577.model.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

class TheContentHashToURI {

    companion object {
        @JvmStatic
        fun provideTestVectors() = testVectors
    }

    @ParameterizedTest
    @MethodSource("provideTestVectors")
    fun `can handle test vectors`(contentHash: String, uri: String) {
        val hex = HexString(contentHash)
        val url = ContentHash(hex.hexToByteArray()).toURI()

        if (url !is SuccessfulToURIResult) {
            fail("toURL was not successful")
        }
        assertThat(url.uri).isEqualTo(uri)

    }

    @Test
    fun `can handle invalid storage`() {
        val hex = HexString("0xde0101deaddead")

        val contentHash = ContentHash(hex.hexToByteArray())

        assertThat(contentHash.toURI()).isInstanceOf(InvalidStorageSystem::class.java)
    }

    @Test
    fun `can handle invalid cid`() {
        val hex = HexString("0xe30102deaddead")

        val contentHash = ContentHash(hex.hexToByteArray())

        assertThat(contentHash.toURI()).isInstanceOf(InvalidCIDError::class.java)
    }

    @Test
    fun `can handle invalid empty array`() {

        val contentHash = ContentHash(ByteArray(0))

        assertThat(contentHash.toURI()).isInstanceOf(ContentTooShort::class.java)
    }
}
