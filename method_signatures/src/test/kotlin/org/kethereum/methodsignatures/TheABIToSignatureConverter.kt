package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.abi.model.EthereumFunction
import org.kethereum.abi.model.EthereumNamedType

class TheABIToSignatureConverter {

    @Test
    fun canConvertABIElementToTextMethodSignature() {
        val tested = EthereumFunction(name = "foo", inputs = emptyList(), outputs = emptyList())
        assertThat(tested.toTextMethodSignature().normalizedSignature).isEqualTo("foo()")

    }

    @Test
    fun canConvertABIElementToTextMethodSignatureWithParameter() {
        val tested = EthereumFunction(name = "yolo", inputs = listOf(EthereumNamedType("foo", "bool")), outputs = emptyList())
        assertThat(tested.toTextMethodSignature().normalizedSignature).isEqualTo("yolo(bool)")

    }
}