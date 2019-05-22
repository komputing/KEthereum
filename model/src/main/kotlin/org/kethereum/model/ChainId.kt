package org.kethereum.model

import java.math.BigInteger

inline class ChainId(val value: BigInteger) {
    constructor(longValue: Long) : this(BigInteger.valueOf(longValue))
}
