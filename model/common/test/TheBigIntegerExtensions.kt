import org.kethereum.extensions.maybeHexToBigInteger
import org.kethereum.extensions.toBytesPadded
import org.kethereum.number.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TheBigIntegerExtensions {

    @Test
    fun paddingWorks() {
        assertEquals(BigInteger("5").toBytesPadded(42).size, 42)
    }

    @Test
    fun maybeHexToBigIntegerWorks() {
        assertEquals("0xa".maybeHexToBigInteger(), BigInteger.TEN)
        assertEquals("10".maybeHexToBigInteger(), BigInteger.TEN)
        assertEquals("0x0".maybeHexToBigInteger(), BigInteger.ZERO)
        assertEquals("0".maybeHexToBigInteger(), BigInteger.ZERO)
        assertEquals("0x1".maybeHexToBigInteger(), BigInteger.ONE)
        assertEquals("1".maybeHexToBigInteger(), BigInteger.ONE)
        assertEquals("1001".maybeHexToBigInteger(), BigInteger.valueOf(1001))

        assertFailsWith(NumberFormatException::class) {
            "a".maybeHexToBigInteger()
        }
        assertFailsWith(NumberFormatException::class) {
            "0x?".maybeHexToBigInteger()
        }
        assertFailsWith(NumberFormatException::class) {
            "yolo".maybeHexToBigInteger()
        }
    }
}