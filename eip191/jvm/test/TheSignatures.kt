package org.kethereum.eip191

import kotlinx.io.core.toByteArray
import org.kethereum.crypto.CryptoAPI
import org.kethereum.crypto.createEthereumKeyPair
import org.kethereum.crypto.impl.BouncyCastleCryptoAPIProvider
import org.kethereum.crypto.signMessage
import kotlin.test.Test
import kotlin.test.assertEquals

val KEY_PAIR = createEthereumKeyPair()

class TheSignatures {

    @Test
    fun createsCorrectPersonalSignSignature() {
        CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider)
        val payload = "Test Payload".toByteArray()

        assertEquals(
            KEY_PAIR.signWithEIP191PersonalSign(payload),
            KEY_PAIR.signMessage(("\u0019Ethereum Signed Message:\n" + payload.size).toByteArray() + payload)
        )
    }
}
