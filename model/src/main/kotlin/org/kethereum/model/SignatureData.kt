package org.kethereum.model

import org.kethereum.extensions.hexToBigInteger
import org.komputing.khex.extensions.clean0xPrefix
import org.komputing.khex.extensions.isValidHex
import org.komputing.khex.model.HexString
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.security.SignatureException

data class SignatureData(
    var r: BigInteger = ZERO,
    var s: BigInteger = ZERO,
    var v: BigInteger = ZERO,
) {
    companion object {
        /**
         * Build SignatureData object from Ethereum message signature in hex format.
         *
         * @param hexSignature Message signature as hex string.
         * @return SignatureData
         * @throws SignatureException If there was a signature format error.
         */
        @Throws(SignatureException::class)
        fun fromHex(hexSignature: String): SignatureData {
            if (!HexString(hexSignature).isValidHex()) {
                throw SignatureException("Invalid hex string: $hexSignature")
            }
            val cleanedHex = HexString(hexSignature).clean0xPrefix().string
            if (cleanedHex.length <= 128) {
                throw SignatureException("Signature hex too short, expected more than 128 digits")
            }
            val r = HexString(cleanedHex.substring(0, 64)).hexToBigInteger()
            val s = HexString(cleanedHex.substring(64, 128)).hexToBigInteger()
            val v = HexString(cleanedHex.substring(128)).hexToBigInteger()
            return SignatureData(r, s, v)
        }
    }
}
