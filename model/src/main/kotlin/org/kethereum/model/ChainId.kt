package org.kethereum.model

import com.ionspin.kotlin.bignum.integer.BigInteger

inline class ChainId(val value: BigInteger) {
    constructor(longValue: Long) : this(BigInteger(longValue))
}
