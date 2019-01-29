package org.kethereum.crypto.impl.ec

import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import org.kethereum.crypto.api.ec.Curve
import org.kethereum.crypto.api.ec.CurvePoint
import java.math.BigInteger

internal val CURVE_PARAMS by lazy { CustomNamedCurves.getByName("secp256k1")!! }
internal val DOMAIN_PARAMS = CURVE_PARAMS.run { ECDomainParameters(curve, g, n, h) }

class EllipticCurve : Curve {

    override val n: BigInteger
        get() = CURVE_PARAMS.n

    override val g: CurvePoint
        get() = CURVE_PARAMS.g.toCurvePoint()

    override fun decodePoint(data: ByteArray): CurvePoint =
            CURVE_PARAMS.curve.decodePoint(data).toCurvePoint()

    override fun createPoint(x: BigInteger, y: BigInteger): CurvePoint =
            CURVE_PARAMS.curve.createPoint(x, y).toCurvePoint()
}
