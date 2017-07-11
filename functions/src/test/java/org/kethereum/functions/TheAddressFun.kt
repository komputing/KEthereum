package org.kethereum.functions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.model.Address

class TheAddressFun {

    @Test
    fun isNotValidWhenTooShort() {
        assertThat(Address("0xF").isValid()).isFalse()
    }

    @Test
    fun isNotValidWhenEmpty() {
        assertThat(Address("").isValid()).isFalse()
    }

    @Test
    fun isValidWhenValid() {
        assertThat(Address("0xfdf1210fc262c73d0436236a0e07be419babbbc4").isValid()).isTrue()
    }

    @Test
    fun isNotValidWhenContainsInvalidChars() {
        assertThat(Address("0xfdf1210fc262c73d0436236a0e07be419babbbcZ").isValid()).isFalse()
    }

    @Test
    fun isNotValidWhenTooLong() {
        assertThat(Address("0xfdf1210fc262c73d0436236a0e07be419babbbc4a").isValid()).isFalse()
    }

}
