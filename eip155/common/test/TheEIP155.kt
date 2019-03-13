package org.kethereum.eip155

import org.kethereum.crypto.toECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.SignatureData
import org.kethereum.model.extensions.hexToByteArray
import kotlin.test.Test
import kotlin.test.assertEquals


val privateKey = PrivateKey("4646464646464646464646464646464646464646464646464646464646464646".hexToByteArray())
val KEY_PAIR = privateKey.toECKeyPair()

class TheEIP155 {

    @Test
    fun canExtractChainIDs() {
        assertEquals(SignatureData().copy(v = 37).extractChainID(), 1)
        assertEquals(SignatureData().copy(v = 38).extractChainID(), 1)
        assertEquals(SignatureData().copy(v = 39).extractChainID(), 2)
        assertEquals(SignatureData().copy(v = 40).extractChainID(), 2)
    }
}