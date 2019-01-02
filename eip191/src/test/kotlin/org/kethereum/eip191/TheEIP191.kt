package org.kethereum.eip191

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.createEthereumKeyPair
import org.kethereum.crypto.signMessage

val KEY_PAIR = createEthereumKeyPair()

class TheSignatures {

    @Test
    fun createsCorrectPersonalSignSignature() {

        val payload = "Test Payload".toByteArray()

        assertThat(KEY_PAIR.signWithEIP191PersonalSign(payload))
                .isEqualTo(KEY_PAIR.signMessage(("\u0019Ethereum Signed Message:\n" + payload.size).toByteArray() + payload))
    }


}
