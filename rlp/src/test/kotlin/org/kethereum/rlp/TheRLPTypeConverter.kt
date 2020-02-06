package org.kethereum.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.math.BigInteger.*

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
            assertThat(it.toRLP().toUnsignedBigIntegerFromRLP()).isEqualTo(it)
        }

        integerTestVectors.forEach {
            assertThat(it.toRLP().toIntFromRLP()).isEqualTo(it)
        }

        assertThat("foo".toRLP().toStringFromRLP()).isEqualTo("foo")

        assertThat(Byte.MAX_VALUE.toRLP().toByteFromRLP()).isEqualTo(Byte.MAX_VALUE)
        assertThat(Byte.MIN_VALUE.toRLP().toByteFromRLP()).isEqualTo(Byte.MIN_VALUE)

    }

}
