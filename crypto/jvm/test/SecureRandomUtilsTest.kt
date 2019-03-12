package org.kethereum.crypto

import org.kethereum.crypto.SecureRandomUtils.secureRandom
import kotlin.test.Test
import kotlin.test.assertFalse

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
