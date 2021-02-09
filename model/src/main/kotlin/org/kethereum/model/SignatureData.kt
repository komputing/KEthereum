package org.kethereum.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ZERO

data class SignatureData(var r: BigInteger = ZERO,
                         var s: BigInteger = ZERO,
                         var v: BigInteger = ZERO)
