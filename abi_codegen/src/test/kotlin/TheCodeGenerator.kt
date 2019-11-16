package org.kethereum.abi_codegen

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.EthereumABI
import org.kethereum.abi_codegen.model.GeneratorSpec
import kotlin.test.assertFails

class TheCodeGenerator {

    @Test
    fun generatesCodeWithSaneBracketsForCommonABIs() {
        listOf("peepeth", "ENS", "ENSResolver", "ERC20").forEach { it ->
            getABI(it, GeneratorSpec(it)).checkForBracketSanity()
        }
    }

    @Test
    fun testBracketSanityChecker() {
        "{}".checkForBracketSanity()
        "{[]}".checkForBracketSanity()
        "{<>[][]}".checkForBracketSanity()

        assertFails {
            "}".checkForBracketSanity()
        }

        assertFails {
            "{<}>".checkForBracketSanity()
        }

        assertFails {
            "{".checkForBracketSanity()
        }
    }

    @Test
    fun classNameWorks() {
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH"))).contains("class PeepETH")
    }

    @Test
    fun packageWorks() {
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH", "eth.peep"))).startsWith("package eth.peep")
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH"))).startsWith("import")
    }

    @Test
    fun internalWorks() {
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH", "eth.peep", true))).contains("internal class")
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH", "eth.peep", false))).doesNotContain("internal class")
    }

    @Test
    fun testCanDisableTransactionDecoder() {
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH"))).contains("TransactionDecoder")
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH", txDecoderName = null))).doesNotContain("TransactionDecoder")
    }

    @Test
    fun testCanDisableRPCConnector() {
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH"))).contains("RPCConnector")
        assertThat(getABI("peepeth", GeneratorSpec("PeepETH", rpcConnectorName = null))).doesNotContain("RPCConnector")
    }

    private fun getABI(name: String, spec: GeneratorSpec) = StringBuilder().apply {
        EthereumABI(javaClass.getResource("/$name.abi").readText()).toKotlinCode(spec).writeTo(this)
    }.toString()

}