package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheParser {

    @Test
    fun convertingTransactionRPC2KethereumTransactionWorks() {
        val parsed = EthereumABI(javaClass.getResource("/peepeth.abi").readText())
        assertThat(parsed.methodList.size).isEqualTo(31)
    }
}