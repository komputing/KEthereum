package org.kethereum.crypto

import kotlinx.io.core.toByteArray
import org.kethereum.crypto.impl.BouncyCastleCryptoAPIProvider
import org.kethereum.crypto.test_data.KEY_PAIR
import org.kethereum.crypto.test_data.PRIVATE_KEY
import org.kethereum.crypto.test_data.PUBLIC_KEY
import org.kethereum.crypto.test_data.TEST_MESSAGE
import org.kethereum.hashes.sha256
import org.kethereum.model.PrivateKey
import org.kethereum.model.SignatureData
import org.kethereum.model.exceptions.SignatureException
import org.kethereum.model.extensions.hexToBigInteger
import org.kethereum.model.extensions.hexToByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SignTest {

    @Test
    fun testSignMessage() {
        CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider)
        val signatureData = KEY_PAIR.signMessage(TEST_MESSAGE)

        val expected = SignatureData(
                "0x9631f6d21dec448a213585a4a41a28ef3d4337548aa34734478b563036163786".hexToBigInteger(),
                "0x2ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b".hexToBigInteger(),
                27.toByte()
        )

        assertEquals(signatureData, expected)
    }

    @Test
    fun testSignMessageHash() {
        CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider)
        val privateKey = PrivateKey("0x65fc670d9351cb87d1f56702fb56a7832ae2aab3427be944ab8c9f2a0ab87960".hexToByteArray())
        val keyPair = privateKey.toECKeyPair()

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
    fun testSignedMessageToKey() {
        assertFailsWith<SignatureException> {
            val signatureData = KEY_PAIR.signMessage(TEST_MESSAGE)
            val key = signedMessageToKey(TEST_MESSAGE, signatureData)
            assertEquals(key, PUBLIC_KEY)
        }
    }

    @Test
    fun testPublicKeyFromPrivateKey() {
        CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider)
        assertEquals(publicKeyFromPrivate(PRIVATE_KEY), PUBLIC_KEY)
    }

}
