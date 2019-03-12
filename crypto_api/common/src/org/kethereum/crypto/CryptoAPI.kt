package org.kethereum.crypto

import org.kethereum.crypto.api.cipher.AESCipher
import org.kethereum.crypto.api.ec.Curve
import org.kethereum.crypto.api.ec.KeyPairGenerator
import org.kethereum.crypto.api.ec.Signer
import org.kethereum.crypto.api.mac.Hmac
import org.kethereum.crypto.impl.kdf.PBKDF2
import org.kethereum.crypto.impl.kdf.SCrypt

object CryptoAPI {

    private lateinit var provider: Provider

    fun setProvider(provider: Provider) {
        this.provider = provider
    }

    private fun <T> lazyCheck(block: () -> T): T {
        if (!::provider.isInitialized) {
            throw UnsupportedOperationException("Please set a provider using CryptoAPI.setProvider")
        }
        return block()
    }

    val hmac: Hmac
        get() { return lazyCheck { provider.hmacProvider() } }

    val keyPairGenerator: KeyPairGenerator
        get() { return lazyCheck { provider.keyPairGeneratorProvider() } }

    val curve: Curve
        get() { return lazyCheck { provider.curveProvider() } }

    val signer: Signer
        get() { return lazyCheck { provider.signerProvider() } }


    val pbkdf2: PBKDF2
        get() { return lazyCheck { provider.pbkf2Provider() } }

    val scrypt: SCrypt
        get() { return lazyCheck { provider.scryptProvider() } }

    val aesCipher: AESCipher
        get() { return lazyCheck { provider.aesCipherProvider() } }

    abstract class Provider {
        abstract fun hmacProvider(): Hmac
        abstract fun keyPairGeneratorProvider(): KeyPairGenerator
        abstract fun curveProvider(): Curve
        abstract fun signerProvider(): Signer
        abstract fun pbkf2Provider(): PBKDF2
        abstract fun scryptProvider(): SCrypt
        abstract fun aesCipherProvider(): AESCipher
    }
}
