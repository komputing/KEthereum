package org.kethereum.crypto.impl.ec

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import org.kethereum.crypto.api.ec.Curve
import org.kethereum.crypto.api.ec.CurvePoint
import org.kethereum.crypto.impl.toJavaBigInteger
import org.kethereum.crypto.impl.toKotlinBigInteger

internal val CURVE_PARAMS by lazy { CustomNamedCurves.getByName("secp256k1")!! }
internal val DOMAIN_PARAMS = CURVE_PARAMS.run { ECDomainParameters(curve, g, n, h) }

class EllipticCurve : Curve {

    override val n: BigInteger
        get() = CURVE_PARAMS.n.toKotlinBigInteger()

    override val g: CurvePoint
        get() = CURVE_PARAMS.g.toCurvePoint()

    override fun decodePoint(data: ByteArray): CurvePoint =
            CURVE_PARAMS.curve.decodePoint(data).toCurvePoint()

    override fun createPoint(x: BigInteger, y: BigInteger): CurvePoint =
            CURVE_PARAMS.curve.createPoint(x.toJavaBigInteger(), y.toJavaBigInteger()).toCurvePoint()
}
