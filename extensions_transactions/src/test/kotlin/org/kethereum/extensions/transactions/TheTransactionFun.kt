package org.kethereum.extensions.transactions

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.createEmptyTransaction
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

class TheTransactionFun {

    val createTokenTransferTransactionInput = createEmptyTransaction().copy(input = HexString("0x40c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea0100000").hexToByteArray())

    @Test
    fun weCanParseTokenTransferValue() {
        assertThat(createTokenTransferTransactionInput.getTokenTransferValue()).isEqualTo(BigInteger.parseString("420"+"0".repeat(18)))
    }

    @Test
    fun weCanParseTokenTransferTo() {
        assertThat (createTokenTransferTransactionInput.getTokenTransferTo()).isEqualTo(Address("0x63ce9f57e2e4b41d3451dec20ddb89143fd755bb"))
    }

}
