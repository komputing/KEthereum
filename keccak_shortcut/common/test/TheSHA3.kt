import org.kethereum.keccakshortcut.SHA3Parameter
import org.kethereum.keccakshortcut.calculateSHA3
import org.kethereum.keccakshortcut.fillWithZero
import org.kethereum.model.extensions.hexToByteArray
import org.kethereum.model.extensions.toHexString
import org.kethereum.model.extensions.toNoPrefixHexString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// TODO: Delete this class once WallETH SHA3 is multiplatform compliant
class TheSHA3 {

    private val allNistTestVectors = listOf(
        testVectors224 to SHA3Parameter.SHA3_224,
        testVectors256 to SHA3Parameter.SHA3_256,
        testVectors384 to SHA3Parameter.SHA3_384,
        testVectors512 to SHA3Parameter.SHA3_512
    )

    @Test
    fun testNistVectors() {
        allNistTestVectors.forEach { (vectorList, params) ->
            vectorList.forEach { (input, expected) ->
                assertEquals(input.hexToByteArray().calculateSHA3(params).toNoPrefixHexString(), expected)
            }
        }
    }

    @Test
    fun keccak256hashesAreCorrect() {

        assertTrue(
            "".calculateSHA3(SHA3Parameter.KECCAK_256)
                .contentEquals("c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470".hexToByteArray())
        )

        assertTrue(
            "The quick brown fox jumps over the lazy dog".calculateSHA3(SHA3Parameter.KECCAK_256)
                .contentEquals("4d741b6f1eb29cb2a9b9911c82f56fa8d73b04959d3d9d222895df6c0b28aa15".hexToByteArray())
        )

        assertTrue(
            "The quick brown fox jumps over the lazy dog.".calculateSHA3(SHA3Parameter.KECCAK_256)
                .contentEquals("578951e24efd62a3d63a86f7cd19aaa53c898fe287d2552133220370240b572d".hexToByteArray())
        )


        assertTrue(
            "The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog.The quick brown fox jumps over the lazy dog."
                .calculateSHA3(SHA3Parameter.KECCAK_256)
                .contentEquals("e35949d2ca446ea2fd99f49bed23c60e0b9849f5384661bc574a5c55fcaeb4bd".hexToByteArray())
        )

        assertEquals(
            "a1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce2270f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113"
                .hexToByteArray().calculateSHA3(SHA3Parameter.KECCAK_256).toHexString(""),
            "e1674295e8fc8ffdfb46cadb01c52b08330e05d731e38c856c1043288f7d9744"
        )
    }

    @Test
    fun fillWithZeroesWorks() {
        assertEquals("5".fillWithZero(3), "500")
        assertEquals("".fillWithZero(5), "00000")
        assertEquals("777".fillWithZero(1), "777")
    }
}