package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.SecureRandomUtils.secureRandom

class SecureRandomUtilsTest {

    @Test
    fun testSecureRandom() {
        secureRandom().nextInt()
    }

    @Test
    fun testIsNotAndroidRuntime() {
        assertThat(SecureRandomUtils.isAndroidRuntime).isFalse()
    }
}
