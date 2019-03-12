package org.kethereum.bip39.wordlists

import kotlin.test.Test
import kotlin.test.assertEquals

class MnemonicTest {

    @Test
    fun throwsOnWrongEntropySize() {
        assertEquals(WORDLIST_CHINESE_TRADITIONAL.size, 2048)
    }
}