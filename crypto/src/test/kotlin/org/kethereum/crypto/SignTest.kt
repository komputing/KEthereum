package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.KEY_PAIR
import org.kethereum.crypto.test_data.PRIVATE_KEY
import org.kethereum.crypto.test_data.PUBLIC_KEY
import org.kethereum.crypto.test_data.TEST_MESSAGE
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.PrivateKey
import org.kethereum.model.SignatureData
import org.komputing.khash.sha256.extensions.sha256
import org.komputing.khex.model.HexString
import java.security.SignatureException

class SignTest {

    @Test
    fun testSignMessage() {
        val signatureData = KEY_PAIR.signMessage(TEST_MESSAGE)

        val expected = SignatureData(
                HexString("0x9631f6d21dec448a213585a4a41a28ef3d4337548aa34734478b563036163786").hexToBigInteger(),
                HexString("0x2ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b").hexToBigInteger(),
                27.toBigInteger()
        )

        assertThat(signatureData).isEqualTo(expected)
    }

    @Test
    fun testSignMessageHash() {
        val privateKey = PrivateKey(HexString("0x65fc670d9351cb87d1f56702fb56a7832ae2aab3427be944ab8c9f2a0ab87960"))
        val keyPair = privateKey.toECKeyPair()

        val messageHash = "Hello, world!".toByteArray().sha256()
        val signatureData = signMessageHash(messageHash, keyPair, false)

        val expected = SignatureData(
                HexString("0x6bcd81446183af193ca4a172d5c5c26345903b24770d90b5d790f74a9dec1f68").hexToBigInteger(),
                HexString("0xe2b85b3c92c9b4f3cf58de46e7997d8efb6e14b2e532d13dfa22ee02f3a43d5d").hexToBigInteger(),
                28.toBigInteger()
        )

        assertThat(expected).isEqualTo(signatureData)
    }

    @Test
    @Throws(SignatureException::class)
    fun testSignedMessageToKey() {
        val signatureData = KEY_PAIR.signMessage(TEST_MESSAGE)
        val key = signedMessageToKey(TEST_MESSAGE, signatureData)
        assertThat(key).isEqualTo(PUBLIC_KEY)
    }

    @Test
    fun testPublicKeyFromPrivateKey() {
        assertThat(publicKeyFromPrivate(PRIVATE_KEY)).isEqualTo(PUBLIC_KEY)
    }

}
