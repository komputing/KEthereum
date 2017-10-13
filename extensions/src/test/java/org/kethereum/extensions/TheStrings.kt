package org.kethereum.extensions

import org.assertj.core.api.Assertions
import org.junit.Test

class TheStrings {

    @Test
    fun detect0xWorks() {
        Assertions.assertThat("2".has0xPrefix()).isEqualTo(false)
        Assertions.assertThat("0xFF".has0xPrefix()).isEqualTo(true)
    }

    @Test
    fun prepend0xWorks() {
        Assertions.assertThat("2".prepend0xPrefix()).isEqualTo("0x2")
        Assertions.assertThat("0xFF".prepend0xPrefix()).isEqualTo("0xFF")
    }

    @Test
    fun clean0xWorks() {
        Assertions.assertThat("2".clean0xPrefix()).isEqualTo("2")
        Assertions.assertThat("0xFF".clean0xPrefix()).isEqualTo("FF")
    }
}