package org.kethereum.hashes

import org.junit.Assert.*
import org.junit.Test
import javax.xml.bind.DatatypeConverter


class Sha256Tests {

    // HASHING

    @Test fun testDigest() {
        testHash("", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
        testHash("Hello world!", "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a")

        val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Proin pulvinar turpis purus, sit amet dapibus magna commodo quis metus."
        testHash(loremIpsum, "60497604d2f6b4df42cea5efb8956f587f81a4ad66fa1b65d9e085224d255036")
    }

    private fun testHash(input: String, expected: String) {
        val inputArray = input.toByteArray()
        val expectedOutput = DatatypeConverter.parseHexBinary(expected)
        assertArrayEquals(expectedOutput, Sha256.Digest().digest(inputArray))
    }


    // PADDING

    @Test
    fun testPaddedLengthDivisibleBy512() {
        val b = byteArrayOf(0, 1, 2, 3, 0)
        val padded = Sha256.Digest().padMessage(b)
        val paddedLengthBits = padded.size * 8
        assertTrue(paddedLengthBits % 512 == 0)
    }

    @Test
    fun testPaddedMessageHas1Bit() {
        val b = ByteArray(64)
        val padded = Sha256.Digest().padMessage(b)
        assertEquals(128.toByte(), padded[b.size])
    }

    @Test
    fun testPaddingAllZero() {
        val b = byteArrayOf(1, 1, 1, 1, 1, 1, 1)
        val padded = Sha256.Digest().padMessage(b)
        for (i in b.size + 1 until padded.size - 8) {
            assertEquals("byte $i not 0", 0, padded[i].toInt())
        }
    }

}