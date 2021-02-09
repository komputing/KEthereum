package org.kethereum.extensions

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ZERO
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ONE
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.TEN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.komputing.khex.model.HexString

class TheBigIntegerExtensions {

    @Test
    fun paddingWorks() {
        assertThat(BigInteger.parseString("5").toBytesPadded(42).size).isEqualTo(42)
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
