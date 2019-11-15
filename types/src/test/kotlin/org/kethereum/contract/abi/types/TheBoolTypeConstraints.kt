package org.kethereum.contract.abi.types

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.contract.abi.types.model.types.BoolETHType
import kotlin.test.assertFailsWith

class TheBoolTypeConstraints {

    @Test
    fun ofStringWorksForGoodInput() {
        assertThat(BoolETHType.ofString("true").toKotlinType()).isEqualTo(true)
        assertThat(BoolETHType.ofString("TRUE").toKotlinType()).isEqualTo(true)
        assertThat(BoolETHType.ofString("True").toKotlinType()).isEqualTo(true)

        assertThat(BoolETHType.ofString("false").toKotlinType()).isEqualTo(false)
        assertThat(BoolETHType.ofString("FALSE").toKotlinType()).isEqualTo(false)
        assertThat(BoolETHType.ofString("False").toKotlinType()).isEqualTo(false)
    }

    @Test
    fun ofStringFailsForInvalid() {
        assertFailsWith<IllegalArgumentException> {
            BoolETHType.ofString("true ")
        }
        assertFailsWith<IllegalArgumentException> {
            BoolETHType.ofString("")
        }
    }
}