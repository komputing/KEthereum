package org.kethereum.functions.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.math.BigInteger.*

class TheRLPTypeConverter {

    @Test
    fun convertingWorks() {

        arrayOf(0, 5, 555555, 200_000, 1_838_383_984).forEach {
            assertThat(it.toRLP().toIntFromRLP()).isEqualTo(it)
        }

        assertThat(ZERO.toRLP().toBigIntegerFromRLP()).isEqualTo(ZERO)
        assertThat(ONE.toRLP().toBigIntegerFromRLP()).isEqualTo(ONE)
        assertThat(TEN.toRLP().toBigIntegerFromRLP()).isEqualTo(TEN)
        assertThat(BigInteger.valueOf(Long.MAX_VALUE).toRLP().toBigIntegerFromRLP())
                .isEqualTo(BigInteger.valueOf(Long.MAX_VALUE))

        assertThat("foo".toRLP().toStringFromRLP()).isEqualTo("foo")

        assertThat(Byte.MAX_VALUE.toRLP().toByteFromRLP()).isEqualTo(Byte.MAX_VALUE)
        assertThat(Byte.MIN_VALUE.toRLP().toByteFromRLP()).isEqualTo(Byte.MIN_VALUE)

    }

}
