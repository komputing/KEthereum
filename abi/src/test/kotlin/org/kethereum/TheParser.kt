package org.kethereum

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.rpc.EthereumABI

class TheParser {

    @Test
    fun convertingTransactionRPC2KethereumTransactionWorks() {
        val parsed = EthereumABI(javaClass.getResource("/peepeth.abi").readText())
        assertThat(parsed.methodList.size).isEqualTo(31)
    }
}