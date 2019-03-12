package org.kethereum.functions

import org.kethereum.model.Address
import org.kethereum.model.createTransactionWithDefaults
import org.kethereum.model.number.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TheTransactionFun {

    private val someAddress = Address("0xfdf1210fc262c73d0436236a0e07be419babbbc4")
    private val anotherAddress = Address("0xfdf1210fc262c73d0436236a0e07be419babbbc7")

    @Test
    fun weCanCreateTokenTransfer() {
        assertTrue(createTokenTransferTransactionInput(someAddress, BigInteger("10")).startsWith(tokenTransferSignature))
    }

    @Test
    fun weCanParseTokenTransferValue() {
        val createTokenTransferTransactionInput = createTransactionWithDefaults(
            value = BigInteger.valueOf(103),
            from = someAddress,
            to = someAddress,
            input = createTokenTransferTransactionInput(someAddress, BigInteger("10"))
        )
        assertEquals(createTokenTransferTransactionInput.getTokenTransferValue(), BigInteger("10"))
    }

    @Test
    fun weCanParseTokenTransferTo() {
        val createTokenTransferTransactionInput = createTransactionWithDefaults(
            value = BigInteger.valueOf(103),
            from = someAddress,
            to = someAddress,
            input = createTokenTransferTransactionInput(anotherAddress, BigInteger("10"))
        )
        assertEquals(createTokenTransferTransactionInput.getTokenTransferTo(), anotherAddress)
    }
}
