package org.kethereum.eip1559.signer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.DEFAULT_GAS_PRICE
import org.kethereum.eip1559.detector.isEIP1559
import org.kethereum.extensions.transactions.*
import org.kethereum.model.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.extensions.toNoPrefixHexString
import org.komputing.khex.model.HexString
import java.math.BigInteger

class TheEIP1559Signer {

    @Test
    fun canSignWeb3JEIP1559Transaction() {

        val signatureData = web3jEIP1559tx.signViaEIP1559(WEB3J_KEY_PAIR)

        val result = web3jEIP1559tx.encode(signatureData).toHexString()
        val expected =
            "0x02f8698206178082162e8310c8e082753094627306090abab3a6e1400e9345bc60c78a8bef577b80c001a0d1f9ee3bdde4d4e0792c7089b84059fb28e17f494556d8a775450b1dd6c318a1a038bd3e2fb9e018528e0a41f57c7a32a8d23b2693e0451aa6ef4519b234466e7f"

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun canSignNethereumEIP1559Transaction() {

        nethereumTestTransactions.forEach { testCase ->
            val txData = testCase.first.split(",")
            val eip1559tx = Transaction().apply {
                chain = txData[0].toBigInteger()
                nonce = txData[1].toBigInteger()
                gasPrice = null
                gasLimit = txData[4].toBigInteger()
                to = Address(txData[5])
                value = txData[6].toBigInteger()
                maxPriorityFeePerGas = txData[2].toBigInteger()
                maxFeePerGas = txData[3].toBigInteger()
                input = HexString(txData[7]).hexToByteArray()
            }


            val signatureData = eip1559tx.signViaEIP1559(NETHEREUM_KEY_PAIR)

            val result = eip1559tx.encode(signatureData).toNoPrefixHexString()

            assertThat(result).isEqualTo(testCase.second)
        }
    }

    @Test
    fun canDetectNonEIP1559Tx() {
        val nonEIP1559tx = Transaction().apply {
            chain = BigInteger.valueOf(1559)
            nonce = BigInteger.ZERO
            gasPrice = DEFAULT_GAS_PRICE
            gasLimit = BigInteger.valueOf(30000)
            to = Address("0x627306090abaB3A6e1400e9345bC60c78a8BEf57")
            value = BigInteger.valueOf(123)
        }

        assertThat(nonEIP1559tx.isEIP1559()).isFalse

    }

}