package org.kethereum.crypto.api.ec

import org.kethereum.model.number.BigInteger

interface Curve {
    val n: BigInteger
    val g: CurvePoint

    fun decodePoint(data: ByteArray): CurvePoint
    fun createPoint(x: BigInteger, y: BigInteger): CurvePoint
}
