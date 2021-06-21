package org.kethereum.eip1559.detector

import io.mockk.every
import io.mockk.mockkClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.DEFAULT_GAS_PRICE
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BlockInformation
import java.math.BigInteger
import kotlin.test.assertFalse
import kotlin.test.assertTrue

val eip1559tx = Transaction().apply {
    chain = BigInteger.valueOf(1559)
    nonce = BigInteger.ZERO
    gasPrice = null
    gasLimit = BigInteger.valueOf(30000)
    to = Address("0x627306090abaB3A6e1400e9345bC60c78a8BEf57")
    value = BigInteger.valueOf(123)
    maxPriorityFeePerGas = BigInteger.valueOf(5678)
    maxFeePerGas = BigInteger.valueOf(1100000)
}

class TheEIP1559Detector {

    @Test
    fun canDetect1559() {
        assertThat(eip1559tx.isEIP1559()).isTrue
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

    @Test
    fun canDetectEIP1559Block() {
        val mockBlock = mockkClass(BlockInformation::class)
        every { mockBlock.baseFeePerGas } returns BigInteger.ONE
        assertTrue(mockBlock.isEIP1559())
    }

    @Test
    fun canDetectNonEIP1559Block() {
        val mockBlock = mockkClass(BlockInformation::class)
        every { mockBlock.baseFeePerGas } returns null
        assertFalse(mockBlock.isEIP1559())
    }


}