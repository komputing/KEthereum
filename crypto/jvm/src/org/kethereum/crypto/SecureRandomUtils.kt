package org.kethereum.crypto

import java.security.SecureRandom

actual object SecureRandomUtils {

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

    actual fun secureRandom(): SecureRandom = SECURE_RANDOM
}