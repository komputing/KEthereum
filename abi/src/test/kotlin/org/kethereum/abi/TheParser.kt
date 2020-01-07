package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheParser {

    @Test
    fun canParseABI() {
        val parsed = EthereumABI(javaClass.getResource("/peepeth.abi").readText())
        assertThat(parsed.methodList.size).isEqualTo(31)
    }
}