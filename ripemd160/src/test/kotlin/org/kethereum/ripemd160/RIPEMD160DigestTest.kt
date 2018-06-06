package org.kethereum.ripemd160

import org.junit.Assert.assertEquals
import org.junit.Test
import org.walleth.khex.toHexString

import kotlin.text.Charsets.US_ASCII

class RIPEMD160DigestTest {

    private val messages = arrayOf(
            "",
            "a",
            "abc",
            "message digest",
            "abcdefghijklmnopqrstuvwxyz",
            "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
            "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
    )

    private val digests = arrayOf(
            "9c1185a5c5e9fc54612808977ee8f548b2258d31",
            "0bdc9d2d256b3ee9daae347be6f4dc835a467ffe",
            "8eb208f7e05d987a9b044a8e98c6b087f15a0bfc",
            "5d0689ef49d2fae572b881b123a85ffa21595f36",
            "f71c27109c692c1b56bbdceb5b9d2865b3708dbc",
            "12a053384a9c0c88e405a06c27dcf49ada62eb2b",
            "b0e20b6e3116640286ed3a87a5713079b21f5189",
            "9b752e45573d4b39f4dbd3323cab82bf63326bfb"
    )

    private val millionADigest = "52783243c1697bdbe16d37f97f68f08325dc1528"

    @Test
    fun simple() {
        val digest = RIPEMD160Digest()
        val resBuf = ByteArray(digest.digestLength)

        for (i in 0 until messages.size - 1) {
            val message = messages[i].toByteArray(US_ASCII)
            digest.update(message, 0, message.size)
            digest.doFinal(resBuf, 0)

            assertEquals(digests[i], resBuf.toHexString(""))
        }
    }

    @Test
    fun millionA() {
        val digest = RIPEMD160Digest()
        val resBuf = ByteArray(digest.digestLength)

        for (i in 0..999999) {
            digest.update('a'.toByte())
        }

        digest.doFinal(resBuf, 0)
        assertEquals(millionADigest, resBuf.toHexString(""))
    }
}