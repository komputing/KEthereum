package org.kethereum.crypto.impl.ec

import org.bouncycastle.crypto.generators.ECKeyPairGenerator
import org.bouncycastle.crypto.params.ECKeyGenerationParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import org.kethereum.crypto.api.ec.KeyPairGenerator
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import java.math.BigInteger
import java.util.*

class EllipticCurveKeyPairGenerator : KeyPairGenerator {
    override fun generate() = ECKeyPairGenerator().run {
        init(ECKeyGenerationParameters(DOMAIN_PARAMS, null))
        generateKeyPair().run {
            val privateKeyValue = (private as ECPrivateKeyParameters).d
            val publicKeyBytes = (public as ECPublicKeyParameters).q.getEncoded(false)
            val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))
            ECKeyPair(PrivateKey(privateKeyValue), PublicKey(publicKeyValue))
        }
    }
}
