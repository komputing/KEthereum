package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.model.EthereumNamedType

class TheABIToSignatureConverter {

    @Test
    fun canConvertABIElementToTextMethodSignature() {
        val tested = createEthereumFun(name = "foo")
        assertThat(tested.toTextMethodSignature().normalizedSignature).isEqualTo("foo()")

    }

    @Test
    fun canConvertABIElementToTextMethodSignatureWithParameter() {
        val tested = createEthereumFun(name = "yolo", inputs = listOf(EthereumNamedType("foo", "bool")))
        assertThat(tested.toTextMethodSignature().normalizedSignature).isEqualTo("yolo(bool)")

    }
}