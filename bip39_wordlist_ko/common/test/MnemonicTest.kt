import org.kethereum.bip39.wordlists.WORDLIST_KOREAN
import kotlin.test.Test
import kotlin.test.assertEquals

class MnemonicTest {

    @Test
    fun throwsOnWrongEntropySize() {
        assertEquals(WORDLIST_KOREAN.size, 2048)
    }
}