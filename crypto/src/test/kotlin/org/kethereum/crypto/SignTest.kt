package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import org.kethereum.crypto.data.KEY_PAIR
import org.kethereum.crypto.data.PRIVATE_KEY
import org.kethereum.crypto.data.PUBLIC_KEY
import org.kethereum.crypto.data.TEST_MESSAGE
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.hashes.sha256
import org.kethereum.model.SignatureData
import org.walleth.khex.hexToByteArray
import java.security.SignatureException

class SignTest {

    @Test
    fun testSignMessage() {
        val signatureData = signMessage(TEST_MESSAGE, KEY_PAIR)

        val expected = SignatureData(
                "0x9631f6d21dec448a213585a4a41a28ef3d4337548aa34734478b563036163786".hexToBigInteger(),
                "0x2ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b".hexToBigInteger(),
                27.toByte()
        )

        assertThat(signatureData).isEqualTo(expected)
    }

    @Test
    fun testSignMessageHash() {
        val keyPair = ECKeyPair.create("0x65fc670d9351cb87d1f56702fb56a7832ae2aab3427be944ab8c9f2a0ab87960".hexToByteArray())

        val messageHash = "Hello, world!".toByteArray().sha256()
        val signatureData = signMessageHash(messageHash, keyPair, false)

        val expected = SignatureData(
                "0x6bcd81446183af193ca4a172d5c5c26345903b24770d90b5d790f74a9dec1f68".hexToBigInteger(),
                "0xe2b85b3c92c9b4f3cf58de46e7997d8efb6e14b2e532d13dfa22ee02f3a43d5d".hexToBigInteger(),
                28.toByte()
        )

        assertEquals(expected, signatureData)
    }

    @Test
    @Throws(SignatureException::class)
    fun testSignedMessageToKey() {
        val signatureData = signMessage(TEST_MESSAGE, KEY_PAIR)
        val key = signedMessageToKey(TEST_MESSAGE, signatureData)
        assertThat(key).isEqualTo(PUBLIC_KEY)
    }

    @Test
    fun testPublicKeyFromPrivateKey() {
        assertThat(publicKeyFromPrivate(PRIVATE_KEY)).isEqualTo(PUBLIC_KEY)
    }

}
