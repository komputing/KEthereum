package org.kethereum.crypto

import org.kethereum.crypto.test_data.ADDRESS
import org.kethereum.crypto.test_data.KEY_PAIR
import kotlin.test.Test
import kotlin.test.assertEquals

class CredentialsTest {

    @Test
    fun testCredentialsFromString() {
        val credentials = KEY_PAIR.toCredentials()

        assertEquals(credentials.address, ADDRESS)
        assertEquals(credentials.ecKeyPair, KEY_PAIR)
   }
}
