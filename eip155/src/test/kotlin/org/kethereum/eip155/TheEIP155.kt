package org.kethereum.eip155

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.toECKeyPair
import org.kethereum.extensions.transactions.encodeLegacyTxRLP
import org.kethereum.extensions.transactions.toTransaction
import org.kethereum.extensions.transactions.toTransactionSignatureData
import org.kethereum.model.*
import org.kethereum.rlp.RLPList
import org.kethereum.rlp.decodeRLP
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString
import java.math.BigInteger


val privateKey = PrivateKey(HexString("4646464646464646464646464646464646464646464646464646464646464646"))
val KEY_PAIR = privateKey.toECKeyPair()

class TheEIP155 {

    @Test
    fun canExtractChainIDs() {
        assertThat(SignatureData().copy(v = BigInteger.valueOf(37)).extractChainID()).isEqualTo(1)
        assertThat(SignatureData().copy(v = BigInteger.valueOf(38)).extractChainID()).isEqualTo(1)
        assertThat(SignatureData().copy(v = BigInteger.valueOf(39)).extractChainID()).isEqualTo(2)
        assertThat(SignatureData().copy(v = BigInteger.valueOf(40)).extractChainID()).isEqualTo(2)
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
        val signatureData = transaction.signViaEIP155(KEY_PAIR, ChainId(1))

        val result = transaction.encodeLegacyTxRLP(signatureData).toHexString()
        val expected = "0xf86c098504a817c800825208943535353535353535353535353535353535353535880" +
                "de0b6b3a76400008025a028ef61340bd939bc2195fe537567866003e1a15d" +
                "3c71ff63e1590620aa636276a067cbe9d8997f761aecb703304b3800ccf55" +
                "5c9f3dc64214b297fb1966a3b6d83"

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun extractFrom() {
        val hex = HexString("0xf86b0a8504a817c80082520894381e247bef0ebc21b6611786c665dd5514dcc31f87470de4df820000802ca0eb663a939118771638352a6fdbdf7287860b362135df51fde41da4303aac771ea05fa601badefb8982025d8b6826f6882efc07722ca9cba6a189b27eb84debbaab")
        val tx = (hex.hexToByteArray().decodeRLP() as RLPList).toTransaction()!!
        val sig = (hex.hexToByteArray().decodeRLP() as RLPList).toTransactionSignatureData()

        assertThat(tx.extractFrom(sig, ChainId(4))).isEqualTo(Address("8a681d2b7400d67966eef4f585b31a7458f96dba"))


    }
}