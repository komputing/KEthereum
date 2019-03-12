package org.kethereum.crypto

import org.kethereum.model.SignatureData
import org.kethereum.model.extensions.toHexString
import org.kethereum.model.extensions.toHexStringZeroPadded
import org.kethereum.model.number.BigInteger

fun SignatureData.toHex() = r.to64BytePaddedHex() + s.to64BytePaddedHex() + v.toHexString()

private fun BigInteger.to64BytePaddedHex() = toHexStringZeroPadded(64, false)