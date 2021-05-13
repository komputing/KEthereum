package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.ADDRESS
import org.kethereum.crypto.test_data.KEY_PAIR

class CredentialsTest {

    @Test
    fun testCredentialsFromString() {
        val credentials = KEY_PAIR.toCredentials()

        assertThat(credentials.address).isEqualTo(ADDRESS)
        assertThat(credentials.ecKeyPair).usingRecursiveComparison().isEqualTo(KEY_PAIR)
   }
}
