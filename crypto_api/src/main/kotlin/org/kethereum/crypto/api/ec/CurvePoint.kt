package org.kethereum.crypto.api.ec

import com.ionspin.kotlin.bignum.integer.BigInteger

interface CurvePoint {
    val x: BigInteger
    val y: BigInteger

    fun mul(n: BigInteger): CurvePoint
    fun add(p: CurvePoint): CurvePoint
    fun normalize(): CurvePoint
    fun isInfinity(): Boolean
    fun encoded(compressed: Boolean = false): ByteArray
}
