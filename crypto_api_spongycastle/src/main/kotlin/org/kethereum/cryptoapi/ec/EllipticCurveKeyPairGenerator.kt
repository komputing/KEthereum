package org.kethereum.cryptoapi.ec

import org.spongycastle.crypto.generators.ECKeyPairGenerator
import org.spongycastle.crypto.params.ECKeyGenerationParameters
import org.spongycastle.crypto.params.ECPrivateKeyParameters
import org.spongycastle.crypto.params.ECPublicKeyParameters
import java.math.BigInteger
import java.util.*

object EllipticCurveKeyPairGenerator : KeyPairGenerator {
    override fun generate(): PrivateAndPublicKey =
        ECKeyPairGenerator().run {
            init(ECKeyGenerationParameters(EllipticCurve.domainParams, null))
            generateKeyPair().run {
                val privateKeyValue = (private as ECPrivateKeyParameters).d
                val publicKeyBytes = (public as ECPublicKeyParameters).q.getEncoded(false)
                val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))
                privateKeyValue to publicKeyValue
            }
        }
}
