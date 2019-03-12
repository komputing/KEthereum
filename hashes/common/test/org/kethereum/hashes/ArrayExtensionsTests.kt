package org.kethereum.hashes

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ArrayExtensionsTests {

    // INT ARRAY CONSTRUCTION

    @Test
    fun testToIntArrayEmpty() {
        val input = byteArrayOf()
        val expected = intArrayOf()
        assertTrue(expected.contentEquals(input.toIntArray()))
    }

    @Test fun testToIntSingle() {
        val input = byteArrayOf(0, 0, 0, 0)
        val expected = intArrayOf(0)
        assertTrue(expected.contentEquals(input.toIntArray()))

        val input2 = byteArrayOf(0, 0, 0, 1)
        val expected2 = intArrayOf(1)
        assertTrue(expected2.contentEquals(input2.toIntArray()))
    }

    @Test fun testToIntArrayMultiple() {
        val input = byteArrayOf(0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3)
        val expected = intArrayOf(1, 2, 3)
        assertTrue(expected.contentEquals(input.toIntArray()))
    }

    @Test
    fun testToIntArrayThrowsForIllegalLength() {
        assertFailsWith(IllegalArgumentException::class) {
            byteArrayOf(0, 0, 0, 1, 0).toIntArray()
        }
    }

    // BYTE ARRAY CONSTRUCTION

    @Test fun testToByteArrayEmpty() {
        val input = intArrayOf()
        val expected = byteArrayOf()

        assertTrue(expected.contentEquals(input.toByteArray()))
    }

    @Test fun testToByteArrayMultiple() {
        val input = intArrayOf(1, 2, 3)
        val expected = byteArrayOf(0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3)

        assertTrue(expected.contentEquals(input.toByteArray()))
    }

    // ARRAY COPY

    @Test fun testIntArrayCopy() {
        val input = intArrayOf(1, 2, 3, 4, 5)
        val destination = IntArray(2)
        input.arrayCopy(1, destination, 0, 2)
        assertTrue(intArrayOf(2, 3).contentEquals(destination))
    }

    // PUT LONG

    @Test fun testByteArrayPutLong() {
        val value = 0xaa_bb_cc_dd_ee_ff_99
        val byteArray = ByteArray(8)
        byteArray.putLong(0, value)
        assertTrue(byteArrayOf(0, -86, -69, -52, -35, -18, -1, -103).contentEquals(byteArray))

        val value2 = -1L
        byteArray.putLong(0, value2)
        assertTrue(byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1).contentEquals(byteArray))
    }
}