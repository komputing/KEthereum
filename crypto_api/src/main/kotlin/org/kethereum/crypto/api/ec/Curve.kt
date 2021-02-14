package org.kethereum.crypto.api.ec

import com.ionspin.kotlin.bignum.integer.BigInteger

interface Curve {
    val n: BigInteger
    val g: CurvePoint

    fun decodePoint(data: ByteArray): CurvePoint
    fun createPoint(x: BigInteger, y: BigInteger): CurvePoint
}
