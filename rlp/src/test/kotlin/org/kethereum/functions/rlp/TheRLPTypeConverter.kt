package org.kethereum.functions.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigInteger
import java.math.BigInteger.*

class TheRLPTypeConverter {

    @Test
    fun convertingWorks() {
        assertThat(0.toRLP().toIntFromRLP()).isEqualTo(0)
        assertThat(5.toRLP().toIntFromRLP()).isEqualTo(5)
        assertThat(555555.toRLP().toIntFromRLP()).isEqualTo(555555)
        assertThat(200000.toRLP().toIntFromRLP()).isEqualTo(200000)

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
