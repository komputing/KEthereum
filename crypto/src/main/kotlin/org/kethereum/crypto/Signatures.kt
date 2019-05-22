package org.kethereum.crypto

import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.model.SignatureData
import java.math.BigInteger

fun SignatureData.toHex() = r.to64BytePaddedHex() + s.to64BytePaddedHex() + v.toHexStringNoPrefix()

private fun BigInteger.to64BytePaddedHex() = toHexStringZeroPadded(64, false)