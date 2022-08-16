package org.kethereum.eip191

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.createEthereumKeyPair
import org.kethereum.crypto.signMessage
import org.kethereum.model.ECKeyPair
import org.kethereum.model.SignatureData
import java.util.UUID

class TheSignatures {

    @Test
    fun createsCorrectPersonalSignSignature() {
        val keyPair = createEthereumKeyPair()
        val payload = "Test Payload".toByteArray()

        assertThat(keyPair.signWithEIP191PersonalSign(payload))
            .isEqualTo(keyPair.personallySign(payload))
    }

    @Test
    fun retrievesCorrectPublicKeyFromPersonalSignature() {
        val keyPair = createEthereumKeyPair()
        val message = UUID.randomUUID().toString().toByteArray()
        val signature = keyPair.personallySign(message)

        val actualKey = personalSignedMessageToPublicKey(message, signature)

        assertThat(actualKey).isEqualTo(keyPair.publicKey)
    }

    private fun ECKeyPair.personallySign(message: ByteArray): SignatureData =
        signMessage(
            "\u0019Ethereum Signed Message:\n${message.size}".toByteArray() + message
        )

}
