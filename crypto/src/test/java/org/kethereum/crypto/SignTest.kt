package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.crypto.data.KEY_PAIR
import org.kethereum.crypto.data.PRIVATE_KEY
import org.kethereum.crypto.data.PUBLIC_KEY
import org.kethereum.crypto.data.TEST_MESSAGE
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.SignatureData
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
