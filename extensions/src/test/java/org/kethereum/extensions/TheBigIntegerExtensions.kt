package org.kethereum.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigInteger
import java.math.BigInteger.*
import kotlin.test.assertFailsWith

class TheBigIntegerExtensions {

    @Test
    fun paddingWorks() {
        assertThat(BigInteger("5").toBytesPadded(42).size).isEqualTo(42)
    }

    @Test
    fun maybeHexToBigIntegerWorks() {
        assertThat("0xa".maybeHexToBigInteger()).isEqualTo(TEN)
        assertThat("10".maybeHexToBigInteger()).isEqualTo(TEN)
        assertThat("0x0".maybeHexToBigInteger()).isEqualTo(ZERO)
        assertThat("0".maybeHexToBigInteger()).isEqualTo(ZERO)
        assertThat("0x1".maybeHexToBigInteger()).isEqualTo(ONE)
        assertThat("1".maybeHexToBigInteger()).isEqualTo(ONE)
        assertThat("1001".maybeHexToBigInteger()).isEqualTo(1001)

        assertFailsWith<NumberFormatException> {
            "a".maybeHexToBigInteger()
        }
        assertFailsWith<NumberFormatException> {
            "0x?".maybeHexToBigInteger()
        }
        assertFailsWith<NumberFormatException> {
            "yolo".maybeHexToBigInteger()
        }
    }
}
