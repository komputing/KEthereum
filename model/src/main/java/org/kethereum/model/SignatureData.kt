package org.kethereum.model

import java.math.BigInteger
import java.math.BigInteger.ZERO

val UNSIGNED = SignatureData()

data class SignatureData(var r: BigInteger, var s: BigInteger, var v: Byte)
{
    constructor() : this(ZERO, ZERO,0)
}