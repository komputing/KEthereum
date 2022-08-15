package org.kethereum.crypto

import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.model.SignatureData
import org.komputing.khex.model.HexString
import java.math.BigInteger

fun HexString.toSignatureData(): SignatureData {
    val r = HexString(string.substring(0, 64)).hexToBigInteger()
    val s = HexString(string.substring(64, 128)).hexToBigInteger()
    val v = HexString(string.substring(128)).hexToBigInteger()
    return SignatureData(r, s, v)
}

fun SignatureData.toHex() = r.to64BytePaddedHex() + s.to64BytePaddedHex() + v.toHexStringNoPrefix()

private fun BigInteger.to64BytePaddedHex() = toHexStringZeroPadded(64, false)