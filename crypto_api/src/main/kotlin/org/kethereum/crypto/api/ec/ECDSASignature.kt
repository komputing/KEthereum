package org.kethereum.crypto.api.ec

import com.ionspin.kotlin.bignum.integer.BigInteger

data class ECDSASignature(val r: BigInteger, val s: BigInteger)
