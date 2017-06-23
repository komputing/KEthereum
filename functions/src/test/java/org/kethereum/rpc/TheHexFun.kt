import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.functions.hexToByteArray
import org.kethereum.functions.toHexString

class TheHexFun {


    @Test
    fun weCanProduceSingleDigitHex() {
        assertThat(0.toByte().toHexString()).isEqualTo("00")
        assertThat(1.toByte().toHexString()).isEqualTo("01")
        assertThat(15.toByte().toHexString()).isEqualTo("0f")
    }

    @Test
    fun weCanProduceDoubleDigitHex() {
        assertThat(16.toByte().toHexString()).isEqualTo("10")
        assertThat(42.toByte().toHexString()).isEqualTo("2a")
        assertThat(255.toByte().toHexString()).isEqualTo("ff")
    }

    @Test
    fun prefixIsIgnored() {
        assertThat("0xab".hexToByteArray()).isEqualTo("ab".hexToByteArray())
    }

    @Test
    fun sizesAreOk() {
        assertThat("0x".hexToByteArray().size).isEqualTo(0)
        assertThat("ff".hexToByteArray().size).isEqualTo(1)
        assertThat("ffaa".hexToByteArray().size).isEqualTo(2)
        assertThat("ffaabb".hexToByteArray().size).isEqualTo(3)
        assertThat("ffaabb44".hexToByteArray().size).isEqualTo(4)
        assertThat("0xffaabb4455".hexToByteArray().size).isEqualTo(5)
        assertThat("0xffaabb445566".hexToByteArray().size).isEqualTo(6)
        assertThat("ffaabb44556677".hexToByteArray().size).isEqualTo(7)
    }

    @Test(expected = IllegalArgumentException::class)
    fun exceptionOnOddInput() {
        "0xa".hexToByteArray()
    }

    @Test
    fun testRoundTrip() {
        assertThat("00".hexToByteArray().toHexString()).isEqualTo("0x00")
        assertThat("ff".hexToByteArray().toHexString()).isEqualTo("0xff")
        assertThat("abcdef".hexToByteArray().toHexString()).isEqualTo("0xabcdef")
        assertThat("0xaa12456789bb".hexToByteArray().toHexString()).isEqualTo("0xaa12456789bb")
    }

}
