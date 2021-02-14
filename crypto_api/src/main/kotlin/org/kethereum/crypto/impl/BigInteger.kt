package org.kethereum.crypto.impl

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.util.fromTwosComplementByteArray
import com.ionspin.kotlin.bignum.integer.util.toTwosComplementByteArray
import java.math.BigInteger as JavaBigInteger

fun JavaBigInteger.toKotlinBigInteger(): BigInteger =
    BigInteger.fromTwosComplementByteArray(toByteArray())

fun BigInteger.toJavaBigInteger(): JavaBigInteger =
    JavaBigInteger(toTwosComplementByteArray())