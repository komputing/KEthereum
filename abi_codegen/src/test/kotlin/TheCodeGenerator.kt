package org.kethereum.abi_codegen

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.EthereumABI
import org.kethereum.abi_codegen.model.GeneratorSpec
import org.kethereum.crypto.test_data.getABIString
import kotlin.test.assertFails

class TheCodeGenerator {

    @Test
    fun generatesCodeWithSaneBracketsForCommonABIs() {
        listOf("peepeth", "ENS", "ENSResolver", "ERC20", "MCD").forEach { it ->
            getABI(it, GeneratorSpec(it)).apply {
                checkForBracketSanity()
                replace(" ", "").also { code ->
                    assertThat(code).doesNotContain("(,")
                    assertThat(code).doesNotContain("<,")
                    assertThat(code).doesNotContain("[,")
                    assertThat(code).doesNotContain(",,")
                    assertThat(code).doesNotContain(",)")
                }
            }
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

        assertThat(getABI("peepeth", GeneratorSpec("PeepETH", "eth.peep", false))).contains("class")
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
        val abiString = getABIString(name)
        EthereumABI(abiString).toKotlinCode(spec).writeTo(this)
    }.toString()
}