package org.kethereum.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.komputing.khex.model.HexString
import java.math.BigInteger
import java.math.BigInteger.*

class TheBigIntegerExtensions {

    @Test
    fun paddingWorks() {
        assertThat(BigInteger("5").toBytesPadded(42).size).isEqualTo(42)
    }

    @Test
    fun maybeHexToBigIntegerWorks() {
        assertThat(HexString("0xa").maybeHexToBigInteger()).isEqualTo(TEN)
        assertThat(HexString("10").maybeHexToBigInteger()).isEqualTo(TEN)
        assertThat(HexString("0x0").maybeHexToBigInteger()).isEqualTo(ZERO)
        assertThat(HexString("0").maybeHexToBigInteger()).isEqualTo(ZERO)
        assertThat(HexString("0x1").maybeHexToBigInteger()).isEqualTo(ONE)
        assertThat(HexString("1").maybeHexToBigInteger()).isEqualTo(ONE)
        assertThat(HexString("1001").maybeHexToBigInteger()).isEqualTo(1001)

        assertThrows(NumberFormatException::class.java) {
            HexString("a").maybeHexToBigInteger()
        }
        assertThrows(NumberFormatException::class.java) {
            HexString("0x?").maybeHexToBigInteger()
        }
        assertThrows(NumberFormatException::class.java) {
            HexString("yolo").maybeHexToBigInteger()
        }
    }
}
