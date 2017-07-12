package org.kethereum.bip44

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheBIP44 {

    @Test(expected = IllegalArgumentException::class)
    fun parsingFailsForBadInput() {
        BIP44.fromPath("abc")
    }

    @Test(expected = IllegalArgumentException::class)
    fun parsingFailsForEmptyInput() {
        BIP44.fromPath("")
    }


    @Test(expected = IllegalArgumentException::class)
    fun parsingFailsForPseudoCorrect() {
        BIP44.fromPath("m")
    }

    val stringProbes = mapOf(
            "m/0" to listOf(BIP44Element(false, 0)),
            "m/0/1" to listOf(BIP44Element(false, 0), BIP44Element(false, 1)),

            "m/44'" to listOf(BIP44Element(true, 44)),
            "m/44'/1" to listOf(BIP44Element(true, 44), BIP44Element(false, 1))
    )


    val intProbes = mapOf(
            "m/0" to listOf(0),
            "m/0/1" to listOf(0, 1),

            "m/0'" to listOf(0x80000000.toInt()),
            "m/1'/1" to listOf(0x80000001.toInt(), 1)
    )


    val dirtyStringProbes = mapOf(
            "m/44 ' " to listOf(BIP44Element(true, 44)),
            "m/0 /1 ' " to listOf(BIP44Element(false, 0), BIP44Element(true, 1))
    )

    @Test
    fun fromPathWork() {
        for ((key, value) in (stringProbes + dirtyStringProbes)) {
            assertThat(BIP44.fromPath(key).path).isEqualTo(value)
        }
    }

    @Test
    fun toStringFromIntoWorks() {
        for ((path, ints) in (intProbes)) {
            assertThat(BIP44.fromPath(path).toIntList()).isEqualTo(ints)
        }
    }


    @Test
    fun toStringWorks() {
        for ((key, value) in (stringProbes)) {
            assertThat(key).isEqualTo(BIP44(value).toString())
        }
    }

    @Test
    fun incrementWorks() {
        assertThat(BIP44.fromPath("m/0/1/2").increment())
                .isEqualTo(BIP44.fromPath("m/0/1/3"))
    }
}