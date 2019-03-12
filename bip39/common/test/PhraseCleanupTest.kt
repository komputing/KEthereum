package org.kethereum.bip39

import org.kethereum.bip39.model.MnemonicWords
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PhraseCleanupTest {

    @Test
    fun fixesCase() {
        assertEquals(dirtyPhraseToMnemonicWords("Foo Bar"), MnemonicWords("foo bar"))
    }

    @Test
    fun trims() {
        assertEquals(dirtyPhraseToMnemonicWords(" jo no "), MnemonicWords("jo no"))
    }

    @Test
    fun removesSpaceInBetween() {
        assertEquals(dirtyPhraseToMnemonicWords(" jo     no "), MnemonicWords("jo no"))
    }

    @Test
    fun allInCombination() {
        assertEquals(dirtyPhraseToMnemonicWords(" Tu     Wat "), MnemonicWords("tu wat"))
    }

    @Test
    fun negativeTest() {
        assertNotEquals(dirtyPhraseToMnemonicWords(" lo ko"), MnemonicWords("jo no"))
    }

}