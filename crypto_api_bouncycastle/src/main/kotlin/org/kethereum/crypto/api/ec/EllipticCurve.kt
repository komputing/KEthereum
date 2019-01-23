package org.kethereum.crypto.api.ec

import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import java.math.BigInteger

object EllipticCurve: Curve {
    val curveParams = CustomNamedCurves.getByName("secp256k1")!!

    internal val domainParams = curveParams.run { ECDomainParameters(curve, g, n, h) }

    override val n: BigInteger
        get() = curveParams.n

    override val g: CurvePoint
        get() = curveParams.g.toCurvePoint()

    override fun decodePoint(data: ByteArray): CurvePoint =
        curveParams.curve.decodePoint(data).toCurvePoint()

    override fun createPoint(x: BigInteger, y: BigInteger): CurvePoint =
        curveParams.curve.createPoint(x, y).toCurvePoint()
}
