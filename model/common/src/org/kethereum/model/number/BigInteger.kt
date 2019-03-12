package org.kethereum.model.number

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
    fun minus(value: BigInteger): BigInteger
    fun xor(value: BigInteger?): BigInteger
    fun and(value: BigInteger?): BigInteger
    fun shiftLeft(value: Int): BigInteger
    fun shiftRight(value: Int): BigInteger

    /**
     * Returns the signum function of this BigInteger.
     */
    fun signum(): Int

    fun toString(radix: Int): String

    fun toByteArray(): ByteArray

    fun toBigDecimal(): BigDecimal

    fun byteValueExact(): Byte


    companion object {
        val ZERO: BigInteger
        val ONE: BigInteger
        val TEN: BigInteger
        fun valueOf(value: Long): BigInteger
    }
}