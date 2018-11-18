package org.kethereum.bip39.wordlists

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MnemonicTest {

    @Test
    fun throwsOnWrongEntropySize() {
        assertEquals(WORDLIST_ENGLISH.size, 2048)
    }

}