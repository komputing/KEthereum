package org.kethereum.functions

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheDataFun {

    @Test
    fun testStartsWith() {
        assertTrue(listOf(0.toByte(),1.toByte()).startsWith(listOf(0.toByte())))
        assertTrue(listOf(0.toByte(),1.toByte()).startsWith(listOf(0.toByte(),1.toByte())))
        assertTrue(listOf<Byte>().startsWith(listOf()))

        assertFalse(listOf(0.toByte(),1.toByte()).startsWith(listOf(2.toByte(),1.toByte())))
        assertFalse(listOf(0.toByte(),1.toByte()).startsWith(listOf(3.toByte())))
        assertFalse(listOf(0.toByte()).startsWith(listOf(2.toByte(),1.toByte())))
    }

}
