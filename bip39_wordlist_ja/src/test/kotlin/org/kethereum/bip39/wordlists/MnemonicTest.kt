package org.kethereum.bip39.wordlists

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MnemonicTest {

    @Test
    fun throwsOnWrongEntropySize() {
        assertEquals(WORDLIST_JAPANESE.size, 2048)
    }

}