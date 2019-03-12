package org.kethereum.number

/**
 *
 */
expect class BigInteger: Number, Comparable<Number> {

    /**
     * Translates the sign-magnitude representation of a BigInteger into a BigInteger.
     */
    constructor(signum: Int, magnitude: ByteArray)

    /**
     * Translates the decimal String representation of a BigInteger into a BigInteger.
     */
    constructor(value: String)

    /**
     * Translates the String representation of a BigInteger in the specified radix into a BigInteger.
     */
    constructor(value: String, radix: Int)

    fun add(value: BigInteger): BigInteger
    fun mod(value: BigInteger): BigInteger

    /**
     * Returns the signum function of this BigInteger.
     */
    fun signum(): Int

    fun toString(radix: Int): String

    fun toByteArray(): ByteArray

    companion object {
        val ZERO: BigInteger
        val ONE: BigInteger
        val TEN: BigInteger
        fun valueOf(value: Long): BigInteger
    }
}