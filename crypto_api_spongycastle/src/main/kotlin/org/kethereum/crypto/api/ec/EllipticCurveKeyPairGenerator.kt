package org.kethereum.crypto.api.ec

import org.kethereum.crypto.model.ECKeyPair
import org.kethereum.crypto.model.PrivateKey
import org.kethereum.crypto.model.PublicKey
import org.spongycastle.crypto.generators.ECKeyPairGenerator
import org.spongycastle.crypto.params.ECKeyGenerationParameters
import org.spongycastle.crypto.params.ECPrivateKeyParameters
import org.spongycastle.crypto.params.ECPublicKeyParameters
import java.math.BigInteger
import java.util.*

object EllipticCurveKeyPairGenerator : KeyPairGenerator {
    override fun generate() = ECKeyPairGenerator().run {
        init(ECKeyGenerationParameters(EllipticCurve.domainParams, null))
        generateKeyPair().run {
            val privateKeyValue = (private as ECPrivateKeyParameters).d
            val publicKeyBytes = (public as ECPublicKeyParameters).q.getEncoded(false)
            val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))
            ECKeyPair(PrivateKey(privateKeyValue), PublicKey(publicKeyValue))
        }
    }
}
