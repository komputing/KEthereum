package org.kethereum.bip39.wordlists

import org.junit.Assert.assertEquals
import org.junit.Test

class MnemonicTest {

    @Test
    fun throwsOnWrongEntropySize() {
        assertEquals(WORDLIST_ENGLISH.size, 2048)
    }

}