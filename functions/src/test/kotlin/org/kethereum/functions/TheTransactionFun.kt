package org.kethereum.functions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.TEST_ADDRESSES
import org.komputing.kethereum.erc20.ERC20TransactionGenerator
import java.math.BigInteger

class TheTransactionFun {

    @Test
    fun weCanParseTokenTransferValue() {
        val createTokenTransferTransactionInput = ERC20TransactionGenerator(TEST_ADDRESSES.first()).transfer(TEST_ADDRESSES.first(), BigInteger("10"))
        assertThat(createTokenTransferTransactionInput.getTokenTransferValue()).isEqualTo(BigInteger("10"))
    }

    @Test
    fun weCanParseTokenTransferTo() {
        val createTokenTransferTransactionInput = ERC20TransactionGenerator(TEST_ADDRESSES.first()).transfer(TEST_ADDRESSES.last(), BigInteger("10"))
        assertThat (createTokenTransferTransactionInput.getTokenTransferTo()).isEqualTo(TEST_ADDRESSES.last())
    }
}
