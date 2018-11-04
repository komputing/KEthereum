package org.kethereum.crypto.ec

import org.spongycastle.math.ec.ECPoint
import java.lang.UnsupportedOperationException
import java.math.BigInteger

interface CurvePoint {
    val x: BigInteger
    val y: BigInteger

    fun mul(n: BigInteger): CurvePoint
    fun add(p: CurvePoint): CurvePoint
    fun normalize(): CurvePoint
    fun isInfinity(): Boolean
    fun encoded(compressed: Boolean = false): ByteArray
}


class SpongyCurvePoint(private val ecPoint: ECPoint) : CurvePoint {
    override val x: BigInteger
        get() = ecPoint.xCoord.toBigInteger()
    override val y: BigInteger
        get() = ecPoint.yCoord.toBigInteger()

    override fun mul(n: BigInteger): CurvePoint =
        ecPoint.multiply(n).toCurvePoint()

    override fun add(p: CurvePoint): CurvePoint =
        (p as? SpongyCurvePoint)?.let {
            ecPoint.add(p.ecPoint).toCurvePoint()
        } ?: throw UnsupportedOperationException("Only SpongyCurvePoint multiplication available")

    override fun normalize(): CurvePoint =
        ecPoint.normalize().toCurvePoint()

    override fun isInfinity(): Boolean =
        ecPoint.isInfinity

    override fun encoded(compressed: Boolean): ByteArray =
        ecPoint.getEncoded(compressed)

}

internal fun ECPoint.toCurvePoint(): CurvePoint = SpongyCurvePoint(this)
