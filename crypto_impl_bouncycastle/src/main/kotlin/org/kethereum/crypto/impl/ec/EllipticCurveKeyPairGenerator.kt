package org.kethereum.crypto.impl.ec

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import org.bouncycastle.crypto.generators.ECKeyPairGenerator
import org.bouncycastle.crypto.params.ECKeyGenerationParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import org.kethereum.crypto.api.ec.KeyPairGenerator
import org.kethereum.crypto.impl.toKotlinBigInteger
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import java.util.*

class EllipticCurveKeyPairGenerator : KeyPairGenerator {
    override fun generate() = ECKeyPairGenerator().run {
        init(ECKeyGenerationParameters(DOMAIN_PARAMS, null))
        generateKeyPair().run {
            val privateKeyValue = (private as ECPrivateKeyParameters).d.toKotlinBigInteger()
            val publicKeyBytes = (public as ECPublicKeyParameters).q.getEncoded(false)
            val publicKeyValue = BigInteger.fromByteArray(Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size), Sign.POSITIVE)
            ECKeyPair(PrivateKey(privateKeyValue), PublicKey(publicKeyValue))
        }
    }
}
