package org.kethereum.rlp

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ONE
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.TEN
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ZERO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

val bigIntegerTestVectors = arrayOf(
        ZERO,
        ONE,
        TEN,
        BigInteger(70_000),
        BigInteger(Long.MAX_VALUE),
        BigInteger(54408193066555392L)
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
