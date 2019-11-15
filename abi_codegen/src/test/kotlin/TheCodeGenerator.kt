package org.kethereum.abi_codegen

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.EthereumABI
import org.kethereum.abi_codegen.model.GeneratorSpec

class TheCodeGenerator {

    @Test
    fun generatesCodeForCommonABIs() {
        listOf("peepeth", "ENS", "ENSResolver").forEach {
            getABI(it, GeneratorSpec(it))
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

    private fun getABI(name: String, spec: GeneratorSpec) = StringBuilder().apply {
        EthereumABI(javaClass.getResource("/$name.abi").readText()).toKotlinCode(spec).writeTo(this)
    }.toString()


}
