package org.kethereum.bloomfilter

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class BloomFilterTest {
    @Test
    fun mightContains() {
        val bloomFilter = BloomFilter(100)
        bloomFilter.add("hello".toByteArray())
        bloomFilter.add("bloom filter".toByteArray())

        assertTrue(bloomFilter.mightContains("hello".toByteArray()))
        assertTrue(bloomFilter.mightContains("bloom filter".toByteArray()))
        assertFalse(bloomFilter.mightContains("xxx".toByteArray()))
    }
}
