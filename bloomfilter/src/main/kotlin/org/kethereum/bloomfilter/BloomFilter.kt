package org.kethereum.bloomfilter

import org.komputing.khash.sha256.extensions.sha256
import java.math.BigInteger
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class BloomFilter(private val size: Int) {
    private val bits = BitSet(size)
    private val lock = ReentrantReadWriteLock()

    fun add(value: ByteArray) {
        lock.write {
            for (seed in 1..3) {
                bits.set(hashing(size, seed, value))
            }
        }
    }

    fun mightContain(value: ByteArray): Boolean {
        lock.read {
            for (seed in 1..3) {
                if (!bits.get(hashing(size, seed, value))) {
                    return false
                }
            }
        }
        return true
    }

    private fun hashing(filterSize: Int, seed: Int, value: ByteArray) =
            BigInteger(1, value.plus(seed.toByte()).sha256())
                    .remainder(BigInteger.valueOf(filterSize.toLong()))
                    .toInt()
}
