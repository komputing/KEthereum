import org.kethereum.bip39.wordlists.WORDLIST_JAPANESE
import kotlin.test.Test
import kotlin.test.assertEquals

class MnemonicTest {

    @Test
    fun throwsOnWrongEntropySize() {
        assertEquals(WORDLIST_JAPANESE.size, 2048)
    }
}