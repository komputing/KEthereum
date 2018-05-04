package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.crypto.data.ADDRESS
import org.kethereum.crypto.data.KEY_PAIR
import org.kethereum.crypto.data.PRIVATE_KEY_STRING
import org.kethereum.crypto.data.PUBLIC_KEY_STRING

class CredentialsTest {

    @Test
    fun testCredentialsFromString() {
        val credentials = Credentials.create(KEY_PAIR)
        verify(credentials)
    }

    @Test
    fun testCredentialsFromECKeyPair() {
        val credentials = Credentials.create(PRIVATE_KEY_STRING, PUBLIC_KEY_STRING)
        verify(credentials)
    }

    private fun verify(credentials: Credentials) {
        assertThat(credentials.address).isEqualTo(ADDRESS)
        assertThat(credentials.ecKeyPair).isEqualToComparingFieldByField(KEY_PAIR)
    }
}
