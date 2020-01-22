package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.model.EthereumNamedType
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature

class TheABIFilter {

    private val peepethFunctions = EthereumABI(javaClass.getResource("/peepeth.abi").readText()).methodList.getAllFunctions()
    private val overloadedFunctions = EthereumABI(javaClass.getResource("/overloaded.abi").readText()).methodList.getAllFunctions()

    @Test
    fun canFilterFunctions() {
        assertThat(peepethFunctions.size).isEqualTo(28)
    }

    @Test
    fun canFindMethodSignature() {
        val function = peepethFunctions.findByTextMethodSignature(TextMethodSignature("isActive()"))?.outputs
        assertThat(function).containsExactly(EthereumNamedType(name = "", type = "bool"))
    }

    @Test
    fun returnsNullWhenSignatureNotFound() {
        val function = peepethFunctions.findByTextMethodSignature(TextMethodSignature("imNotHere()"))?.outputs
        assertThat(function).isNull()
    }

    @Test
    fun returnsNullWhenSignatureDiffersInParams() {
        val function = peepethFunctions.findByTextMethodSignature(TextMethodSignature("isActive(bool)"))?.outputs
        assertThat(function).isNull()
    }

    @Test
    fun canFindMethodOverloadedSignatures() {
        val function2 = overloadedFunctions.findByTextMethodSignature(TextMethodSignature("fuName(bool)"))?.outputs
        assertThat(function2).containsExactly(EthereumNamedType(name = "secondFunOut", type = "bool"))
        val function1 = overloadedFunctions.findByTextMethodSignature(TextMethodSignature("fuName(address)"))?.outputs
        assertThat(function1).containsExactly(EthereumNamedType(name = "firstFunOut", type = "bool"))

    }

    @Test
    fun canFindMethodOverloadedSignaturesByHexMethodSignature() {
        val function2 = overloadedFunctions.findByHexMethodSignature(TextMethodSignature("fuName(bool)").toHexSignature())?.outputs
        assertThat(function2).containsExactly(EthereumNamedType(name = "secondFunOut", type = "bool"))
        val function1 = overloadedFunctions.findByHexMethodSignature(TextMethodSignature("fuName(address)").toHexSignature())?.outputs
        assertThat(function1).containsExactly(EthereumNamedType(name = "firstFunOut", type = "bool"))

    }
}