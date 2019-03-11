package org.kethereum.crypto

import java.security.SecureRandom

/**
 * Utility class for working with SecureRandom implementation.
 *
 *
 * This is to address issues with SecureRandom on Android. For more information, refer to the
 * following [issue](https://github.com/web3j/web3j/issues/146).
 */
object SecureRandomUtils {

    private val SECURE_RANDOM: SecureRandom

    // Taken from BitcoinJ implementation
    // https://github.com/bitcoinj/bitcoinj/blob/3cb1f6c6c589f84fe6e1fb56bf26d94cccc85429/core/src/main/java/org/bitcoinj/core/Utils.java#L573
    internal val isAndroidRuntime by lazy {
        System.getProperty("java.runtime.name").let {
            it != null && it == "Android Runtime"
        }
    }

    init {
        if (isAndroidRuntime) {
            LinuxSecureRandom()
        }
        SECURE_RANDOM = SecureRandom()
    }

    fun secureRandom() = SECURE_RANDOM
}
