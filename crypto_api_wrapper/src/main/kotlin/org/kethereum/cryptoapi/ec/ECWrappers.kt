package org.kethereum.cryptoapi.ec

fun curve(): Curve = EllipticCurve

fun signer(): Signer = EllipticCurveSigner

fun keyPairGenerator(): KeyPairGenerator = EllipticCurveKeyPairGenerator
