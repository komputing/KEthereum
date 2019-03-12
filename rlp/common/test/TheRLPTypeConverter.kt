package org.kethereum.functions.rlp

import org.kethereum.model.number.BigInteger
import org.kethereum.model.number.BigInteger.Companion.ONE
import org.kethereum.model.number.BigInteger.Companion.TEN
import org.kethereum.model.number.BigInteger.Companion.ZERO
import kotlin.test.Test
import kotlin.test.assertEquals

val bigIntegerTestVectors = arrayOf(
        ZERO,
        ONE,
        TEN,
        BigInteger.valueOf(70_000),
        BigInteger.valueOf(Long.MAX_VALUE),
        BigInteger.valueOf(54408193066555392L)
)

val integerTestVectors = arrayOf(
        0,
        5,
        555_555,
        200_000,
        1_838_383_984,
        -1_838_383_984
)

class TheRLPTypeConverter {

    @Test
    fun convertingWorks() {

        bigIntegerTestVectors.forEach {
            assertEquals(it.toRLP().toUnsignedBigIntegerFromRLP(), it)
        }

        integerTestVectors.forEach {
            assertEquals(it.toRLP().toIntFromRLP(), it)
        }

        assertEquals("foo".toRLP().toStringFromRLP(), "foo")
        assertEquals(Byte.MAX_VALUE.toRLP().toByteFromRLP(), Byte.MAX_VALUE)
        assertEquals(Byte.MIN_VALUE.toRLP().toByteFromRLP(), Byte.MIN_VALUE)
    }
}
