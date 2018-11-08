package org.kethereum.crypto.api.ec

fun curve(): Curve = EllipticCurve

fun signer(): Signer = EllipticCurveSigner

fun keyPairGenerator(): KeyPairGenerator = EllipticCurveKeyPairGenerator
