package org.kethereum.model

import java.math.BigInteger

@JvmInline
value class ChainId(val value: BigInteger) {
    constructor(longValue: Long) : this(BigInteger.valueOf(longValue))
}
