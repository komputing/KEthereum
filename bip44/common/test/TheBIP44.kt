package org.kethereum.bip44

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TheBIP44 {

    @Test
    fun parsingFailsForBadInput() {
        assertFailsWith(IllegalArgumentException::class){
            BIP44("abc")
        }
    }

    @Test
    fun parsingFailsForEmptyInput() {
        assertFailsWith(IllegalArgumentException::class){
            BIP44("")
        }
    }


    @Test
    fun parsingFailsForPseudoCorrect() {
        assertFailsWith(IllegalArgumentException::class){
            BIP44("m")
        }
    }

    private val stringProbes = mapOf(
            "m/0" to listOf(BIP44Element(false, 0)),
            "m/0/1" to listOf(BIP44Element(false, 0), BIP44Element(false, 1)),

            "m/44'" to listOf(BIP44Element(true, 44)),
            "m/44'/1" to listOf(BIP44Element(true, 44), BIP44Element(false, 1))
    )


    private val intProbes = mapOf(
            "m/0" to listOf(0),
            "m/0/1" to listOf(0, 1),

            "m/0'" to listOf(0x80000000.toInt()),
            "m/1'/1" to listOf(0x80000001.toInt(), 1)
    )


    private val dirtyStringProbes = mapOf(
            "m/44 ' " to listOf(BIP44Element(true, 44)),
            "m/0 /1 ' " to listOf(BIP44Element(false, 0), BIP44Element(true, 1))
    )

    @Test
    fun fromPathWork() {
        for ((key, value) in (stringProbes + dirtyStringProbes)) {
            assertEquals(BIP44(key).path, value)
        }
    }

    @Test
    fun toStringFromIntoWorks() {
        for ((path, ints) in (intProbes)) {
            assertEquals(BIP44(path).path.map { it.numberWithHardeningFlag }, ints)
        }
    }


    @Test
    fun toStringWorks() {
        for ((key, value) in (stringProbes)) {
            assertEquals(key, BIP44(value).toString())
        }
    }

    @Test
    fun incrementWorks() {
        assertEquals(BIP44("m/0/1/2").increment(), BIP44("m/0/1/3"))
    }
}