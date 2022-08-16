package org.kethereum.model

import org.kethereum.extensions.hexToBigInteger
import org.komputing.khex.extensions.clean0xPrefix
import org.komputing.khex.model.HexString
import java.math.BigInteger
import java.math.BigInteger.ZERO

data class SignatureData(
    var r: BigInteger = ZERO,
    var s: BigInteger = ZERO,
    var v: BigInteger = ZERO,
) {
    companion object {
        fun fromHex(hexSignature: String): SignatureData {
            val cleanedHex = HexString(hexSignature).clean0xPrefix().string
            val r = HexString(cleanedHex.substring(0, 64)).hexToBigInteger()
            val s = HexString(cleanedHex.substring(64, 128)).hexToBigInteger()
            val v = HexString(cleanedHex.substring(128)).hexToBigInteger()
            return SignatureData(r, s, v)
        }
    }
}
