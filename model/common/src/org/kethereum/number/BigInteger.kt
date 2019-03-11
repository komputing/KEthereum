package org.kethereum.number

/**
 *
 */
expect class BigInteger: Comparable<BigInteger> {

    // TODO: Fix these names
    constructor(v: Int, k: ByteArray)

    fun add(value: BigInteger): BigInteger
    fun mod(value: BigInteger): BigInteger

    companion object {
        val ZERO: BigInteger
    }
}