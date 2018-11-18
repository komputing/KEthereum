package org.kethereum.crypto

import org.junit.Assert.assertFalse
import org.junit.jupiter.api.Test
import org.kethereum.crypto.SecureRandomUtils.secureRandom

class SecureRandomUtilsTest {

    @Test
    fun testSecureRandom() {
        secureRandom().nextInt()
    }

    @Test
    fun testIsNotAndroidRuntime() {
        assertFalse(SecureRandomUtils.isAndroidRuntime)
    }
}
