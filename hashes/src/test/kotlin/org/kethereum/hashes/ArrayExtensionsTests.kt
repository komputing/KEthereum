package org.kethereum.hashes

import org.junit.Assert
import org.junit.Test

class ArrayExtensionsTests {

    // INT ARRAY CONSTRUCTION

    @Test fun testToIntArrayEmpty() {
        val input = byteArrayOf()
        val expected = intArrayOf()
        Assert.assertArrayEquals(expected, input.toIntArray())
    }

    @Test fun testToIntSingle() {
        val input = byteArrayOf(0, 0, 0, 0)
        val expected = intArrayOf(0)
        Assert.assertArrayEquals(expected, input.toIntArray())

        val input2 = byteArrayOf(0, 0, 0, 1)
        val expected2 = intArrayOf(1)
        Assert.assertArrayEquals(expected2, input2.toIntArray())
    }

    @Test fun testToIntArrayMultiple() {
        val input = byteArrayOf(0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3)
        val expected = intArrayOf(1, 2, 3)
        Assert.assertArrayEquals(expected, input.toIntArray())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testToIntArrayThrowsForIllegalLength() {
        byteArrayOf(0, 0, 0, 1, 0).toIntArray()
    }

    // BYTE ARRAY CONSTRUCTION

    @Test fun testToByteArrayEmpty() {
        val input = intArrayOf()
        val expected = byteArrayOf()

        Assert.assertArrayEquals(expected, input.toByteArray())
    }

    @Test fun testToByteArrayMultiple() {
        val input = intArrayOf(1, 2, 3)
        val expected = byteArrayOf(0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3)

        Assert.assertArrayEquals(expected, input.toByteArray())
    }

}