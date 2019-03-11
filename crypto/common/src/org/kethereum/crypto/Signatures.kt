package org.kethereum.crypto

import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.model.SignatureData
import org.walleth.khex.toHexString
import java.math.BigInteger

fun SignatureData.toHex() = r.to64BytePaddedHex() + s.to64BytePaddedHex() + v.toHexString()

private fun BigInteger.to64BytePaddedHex() = toHexStringZeroPadded(64, false)