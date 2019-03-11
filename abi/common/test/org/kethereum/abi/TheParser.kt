package org.kethereum.abi

import kotlin.test.Test
import kotlin.test.assertEquals

class TheParser {

    @Test
    fun convertingTransactionRPC2KethereumTransactionWorks() {
        val parsed = EthereumABI(peepetAbi)
        assertEquals(parsed.methodList.size, 31)
    }
}