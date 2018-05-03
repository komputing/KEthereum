package org.kethereum.functions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheDataFun {

    @Test
    fun testStartsWith() {
        assertThat(listOf(0.toByte(),1.toByte()).startsWith(listOf(0.toByte())))
                .isTrue()
        assertThat(listOf(0.toByte(),1.toByte()).startsWith(listOf(0.toByte(),1.toByte())))
                .isTrue()
        assertThat(listOf<Byte>().startsWith(listOf()))
                .isTrue()

        assertThat(listOf(0.toByte(),1.toByte()).startsWith(listOf(2.toByte(),1.toByte())))
                .isFalse()
        assertThat(listOf(0.toByte(),1.toByte()).startsWith(listOf(3.toByte())))
                .isFalse()
        assertThat(listOf(0.toByte()).startsWith(listOf(2.toByte(),1.toByte())))
                .isFalse()
    }

}
