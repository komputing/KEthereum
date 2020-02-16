package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheParser {

    @Test
    fun canParseABI() {
        listOf("/peepeth.abi" to 31,
                "/ENS.abi" to 11,
                "/ENSResolver.abi" to 29,
                "/MCD.abi" to 8,
                "/ERC20.abi" to 12
        ).forEach {
            val parsed = EthereumABI(javaClass.getResource(it.first).readText())
            assertThat(parsed.methodList.size).isEqualTo(it.second)
        }
    }
}