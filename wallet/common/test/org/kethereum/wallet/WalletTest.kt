package org.kethereum.wallet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WalletTest {

    @Test
    fun testGenerateRandomBytes() {
        assertTrue(generateRandomBytes(0).contentEquals(byteArrayOf()))
        assertEquals(generateRandomBytes(10).size, 10)
    }
}