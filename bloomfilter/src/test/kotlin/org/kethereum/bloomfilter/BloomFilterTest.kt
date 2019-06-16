package org.kethereum.bloomfilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class BloomFilterTest {
    @Test
    fun mightContain() {
        val bloomFilter = BloomFilter(100)
        bloomFilter.add("hello".toByteArray())
        bloomFilter.add("bloom filter".toByteArray())

        assertTrue(bloomFilter.mightContain("hello".toByteArray()))
        assertTrue(bloomFilter.mightContain("bloom filter".toByteArray()))
        assertFalse(bloomFilter.mightContain("xxx".toByteArray()))
    }
}
