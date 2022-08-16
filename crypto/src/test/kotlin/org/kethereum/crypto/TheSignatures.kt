package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.SignatureData
import org.komputing.khex.model.HexString
import java.security.SignatureException

private const val R = "0x0031f6d21dec448a213585a4a41a28ef3d4337548aa34734478b563036163786"
private const val S = "0x2ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b"
private const val V = "0x1b"
private const val SIGNATURE =
    "0031f6d21dec448a213585a4a41a28ef3d4337548aa34734478b5630361637862ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b1b"

private val signatureData = SignatureData(
    HexString(R).hexToBigInteger(),
    HexString(S).hexToBigInteger(),
    HexString(V).hexToBigInteger()
)

class TheSignatures {

    @Test
    fun createsCorrectSignatureHex() {
        assertThat(signatureData.toHex()).isEqualTo(SIGNATURE)
    }

    @Test
    fun parsesHexToCorrectSignature() {
        assertThat(SignatureData.fromHex(SIGNATURE)).isEqualTo(signatureData)
    }

    @Test
    fun parsesHexWithPrefixToCorrectSignature() {
        assertThat(SignatureData.fromHex("0x$SIGNATURE")).isEqualTo(signatureData)
    }

    @Test
    fun throwsForInvalidHex() {
        assertThatThrownBy { SignatureData.fromHex("${SIGNATURE}w") }
            .isInstanceOf(SignatureException::class.java)
            .hasMessageContaining("Invalid hex string")
    }

    @Test
    fun throwsForShorterSignature() {
        assertThatThrownBy { SignatureData.fromHex(SIGNATURE.substring(5)) }
            .isInstanceOf(SignatureException::class.java)
            .hasMessageContaining("Signature hex too short")
    }
}
