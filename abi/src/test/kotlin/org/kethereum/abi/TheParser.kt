package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.model.EthereumABIElement
import org.kethereum.abi.model.StateMutability
import org.kethereum.abi.model.StateMutability.*

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


    @Test
    fun canResolveStateMutability() {
        val peepethABI = EthereumABI(javaClass.getResource("/peepeth.abi").readText())

        assertThat(peepethABI.methodList.first { it.name == "updateAccount" }.stateMutability == nonpayable)
        assertThat(peepethABI.methodList.first { it.name == "isValidName" }.stateMutability == pure)
        assertThat(peepethABI.methodList.first { it.name == "owner" }.stateMutability == view)
        assertThat(peepethABI.methodList.first { it.name == "tip" }.stateMutability == payable)

    }

}