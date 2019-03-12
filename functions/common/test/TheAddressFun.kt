package org.kethereum.functions

import org.kethereum.model.Address
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheAddressFun {

    @Test
    fun isNotValidWhenTooShort() {
        assertFalse(Address("0xF").isValid())
    }

    @Test
    fun isNotValidWhenEmpty() {
        assertFalse(Address("").isValid())
    }

    @Test
    fun isValidWhenValid() {
        assertTrue(Address("0xfdf1210fc262c73d0436236a0e07be419babbbc4").isValid())
    }

    @Test
    fun isNotValidWhenContainsInvalidChars() {
        assertFalse(Address("0xfdf1210fc262c73d0436236a0e07be419babbbcZ").isValid())
    }

    @Test
    fun isNotValidWhenTooLong() {
        assertFalse(Address("0xfdf1210fc262c73d0436236a0e07be419babbbc4a").isValid())
    }

}
