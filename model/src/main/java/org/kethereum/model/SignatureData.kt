package org.kethereum.model

import java.math.BigInteger

data class SignatureData(var r: BigInteger, val s: BigInteger, val v: Byte)
