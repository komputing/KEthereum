package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.extensions.hexToBigInteger
import org.kethereum.model.SignatureData

class TheSignatures {

    @Test
    fun createsCorrectSignatureHex() {

        val signatureData = SignatureData(
                "0x0031f6d21dec448a213585a4a41a28ef3d4337548aa34734478b563036163786".hexToBigInteger(),
                "0x2ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b".hexToBigInteger(),
                27.toByte()
        )

        assertThat(signatureData.toHex()).isEqualTo("0031f6d21dec448a213585a4a41a28ef3d4337548aa34734478b5630361637862ff816ee6bbb82719e983ecd8a33a4b45d32a4b58377ef1381163d75eedc900b1b")
    }


}
