package org.kethereum.number

import java.math.BigInteger

actual class BigInteger(private val value: BigInteger): Number(), Comparable<Number> {

    /**
     * Translates the sign-magnitude representation of a BigInteger into a BigInteger.
     */
    actual constructor(signum: Int, magnitude: ByteArray): this(BigInteger(signum, magnitude))

    /**
     * Translates the decimal String representation of a BigInteger into a BigInteger.
     */
    actual constructor(value: String): this(BigInteger(value))

    /**
     * Translates the String representation of a BigInteger in the specified radix into a BigInteger.
     */
    actual constructor(value: String, radix: Int): this(BigInteger(value, radix))

    actual fun add(value: org.kethereum.number.BigInteger): org.kethereum.number.BigInteger {
        this.value.add(value.value)
        return this
    }

    actual fun mod(value: org.kethereum.number.BigInteger): org.kethereum.number.BigInteger {
        this.value.mod(value.value)
        return this
    }

    /**
     * Returns the signum function of this BigInteger.
     */
    actual fun signum(): Int {
        return value.signum()
    }

    actual fun toString(radix: Int): String {
        return value.toString(radix)
    }

    actual fun toByteArray(): ByteArray {
        return value.toByteArray()
    }

    override fun toByte(): Byte = this.value.toByte()

    override fun toChar(): Char = this.value.toChar()

    override fun toDouble(): Double = this.value.toDouble()

    override fun toFloat(): Float = this.value.toFloat()

    override fun toInt(): Int = this.value.toInt()

    override fun toLong(): Long = this.value.toLong()

    override fun toShort(): Short = this.value.toShort()

    actual companion object {
        actual val ZERO: org.kethereum.number.BigInteger = BigInteger(BigInteger.ZERO)
        actual val ONE: org.kethereum.number.BigInteger = BigInteger(BigInteger.ONE)
        actual val TEN: org.kethereum.number.BigInteger = BigInteger(BigInteger.TEN)
        actual fun valueOf(value: Long): org.kethereum.number.BigInteger {
            return BigInteger(BigInteger.valueOf(value))
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is org.kethereum.number.BigInteger && this.value == other.value
    }

    override fun compareTo(other: Number): Int {
        return value.compareTo(BigInteger(other.toString()))
    }
}

