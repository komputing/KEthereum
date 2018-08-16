package org.kethereum.bip39.model

import java.util.*

// TODO inline class once Kotlin 1.3 is out
data class MnemonicWords(val words: Array<String>) {
    constructor(phrase: String) : this(phrase.split(" "))
    constructor(phrase: List<String>) : this(phrase.toTypedArray())

    override fun toString() = words.joinToString(" ")
    override fun equals(other: Any?) = toString() == other?.toString()
    override fun hashCode() = Arrays.hashCode(words)
}