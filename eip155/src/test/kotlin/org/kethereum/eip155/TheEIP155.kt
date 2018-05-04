package org.kethereum.eip155

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.crypto.ECKeyPair
import org.kethereum.functions.encodeRLP
import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString
import java.math.BigInteger


val KEY_PAIR = ECKeyPair.create("4646464646464646464646464646464646464646464646464646464646464646".hexToByteArray())

class TheEIP155 {

    @Test
    fun canExtractChainIDs() {
        assertThat(SignatureData().copy(v = 37).extractChainID()).isEqualTo(1)
        assertThat(SignatureData().copy(v = 38).extractChainID()).isEqualTo(1)
        assertThat(SignatureData().copy(v = 39).extractChainID()).isEqualTo(2)
        assertThat(SignatureData().copy(v = 40).extractChainID()).isEqualTo(2)
    }


    @Test
    fun signTransaction() {
        val transaction = Transaction().apply {
            nonce = BigInteger.valueOf(9)
            gasPrice = BigInteger.valueOf(20000000000L)
            gasLimit = BigInteger.valueOf(21000)
            to = Address("0x3535353535353535353535353535353535353535")
            value = BigInteger.valueOf(1000000000000000000L)
        }
        val signatureData = transaction.signViaEIP155(KEY_PAIR, ChainDefinition(1L))

        val result = transaction.encodeRLP(signatureData).toHexString()
        val expected = "0xf86c098504a817c800825208943535353535353535353535353535353535353535880" +
                "de0b6b3a76400008025a028ef61340bd939bc2195fe537567866003e1a15d" +
                "3c71ff63e1590620aa636276a067cbe9d8997f761aecb703304b3800ccf55" +
                "5c9f3dc64214b297fb1966a3b6d83"

        assertThat(result).isEqualTo(expected)
    }
}