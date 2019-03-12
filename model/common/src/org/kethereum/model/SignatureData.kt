package org.kethereum.model

import org.kethereum.model.number.BigInteger
import org.kethereum.model.number.BigInteger.Companion.ZERO

data class SignatureData(var r: BigInteger = ZERO,
                         var s: BigInteger = ZERO,
                         var v: Byte = 0)
