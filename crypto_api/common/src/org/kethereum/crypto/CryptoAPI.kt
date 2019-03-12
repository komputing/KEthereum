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

    private fun <T> lazyCheck(block: () -> T): Lazy<T> {
        if (!::provider.isInitialized) {
            throw UnsupportedOperationException("Please set a provider using CryptoAPI.setProvider")
        }
        return lazy { block() }
    }

    val hmac by lazyCheck { provider.hmacProvider() }

    val keyPairGenerator by lazyCheck { provider.keyPairGeneratorProvider() }
    val curve by lazyCheck { provider.curveProvider() }
    val signer by lazyCheck { provider.signerProvider() }


    val pbkdf2 by lazyCheck { provider.pbkf2Provider() }
    val scrypt by lazyCheck { provider.scryptProvider() }

    val aesCipher by lazyCheck { provider.aesCipherProvider() }

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
