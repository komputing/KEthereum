package org.kethereum.crypto.impl

import org.kethereum.crypto.CryptoAPI
import org.kethereum.crypto.api.cipher.AESCipher
import org.kethereum.crypto.api.ec.Curve
import org.kethereum.crypto.api.ec.KeyPairGenerator
import org.kethereum.crypto.api.ec.Signer
import org.kethereum.crypto.api.mac.Hmac
import org.kethereum.crypto.impl.cipher.AESCipherImpl
import org.kethereum.crypto.impl.ec.EllipticCurve
import org.kethereum.crypto.impl.ec.EllipticCurveKeyPairGenerator
import org.kethereum.crypto.impl.ec.EllipticCurveSigner
import org.kethereum.crypto.impl.kdf.PBKDF2
import org.kethereum.crypto.impl.kdf.PBKDF2Impl
import org.kethereum.crypto.impl.kdf.SCrypt
import org.kethereum.crypto.impl.kdf.SCryptImpl
import org.kethereum.crypto.impl.mac.HmacImpl

object BouncyCastleCryptoAPIProvider: CryptoAPI.Provider() {

    override fun hmacProvider(): Hmac = HmacImpl()

    override fun keyPairGeneratorProvider(): KeyPairGenerator = EllipticCurveKeyPairGenerator()

    override fun curveProvider(): Curve = EllipticCurve()

    override fun signerProvider(): Signer = EllipticCurveSigner()

    override fun pbkf2Provider(): PBKDF2 = PBKDF2Impl()

    override fun scryptProvider(): SCrypt = SCryptImpl()

    override fun aesCipherProvider(): AESCipher = AESCipherImpl()
}