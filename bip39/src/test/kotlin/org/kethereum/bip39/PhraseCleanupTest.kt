package org.kethereum.bip39

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.bip39.model.MnemonicWords

class PhraseCleanupTest {

    @Test
    fun fixesCase() {
        assertThat(dirtyPhraseToMnemonicWords("Foo Bar"))
                .isEqualTo(MnemonicWords("foo bar"))
    }


    @Test
    fun trims() {
        assertThat(dirtyPhraseToMnemonicWords(" jo no "))
                .isEqualTo(MnemonicWords("jo no"))
    }

    @Test
    fun removesSpaceInBetween() {
        assertThat(dirtyPhraseToMnemonicWords(" jo     no "))
                .isEqualTo(MnemonicWords("jo no"))
    }

    @Test
    fun allInCombination() {
        assertThat(dirtyPhraseToMnemonicWords(" Tu     Wat "))
                .isEqualTo(MnemonicWords("tu wat"))
    }

    @Test
    fun negativeTest() {
        assertThat(dirtyPhraseToMnemonicWords(" lo ko"))
                .isNotEqualTo(MnemonicWords("jo no"))
    }

}