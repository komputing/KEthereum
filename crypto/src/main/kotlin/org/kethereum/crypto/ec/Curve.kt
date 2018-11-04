package org.kethereum.crypto.ec

import org.spongycastle.crypto.ec.CustomNamedCurves
import java.math.BigInteger

interface Curve {
    val n: BigInteger
    val g: CurvePoint

    fun decodePoint(data: ByteArray): CurvePoint
    fun createPoint(x: BigInteger, y: BigInteger): CurvePoint
}

class SpongyEllipticCurve: Curve {
    val curveParams = CustomNamedCurves.getByName("secp256k1")!!

    override val n: BigInteger
        get() = curveParams.n

    override val g: CurvePoint
        get() = curveParams.g.toCurvePoint()

    override fun decodePoint(data: ByteArray): CurvePoint =
        curveParams.curve.decodePoint(data).toCurvePoint()

    override fun createPoint(x: BigInteger, y: BigInteger): CurvePoint =
        curveParams.curve.createPoint(x, y).toCurvePoint()
}
