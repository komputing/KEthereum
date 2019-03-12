package org.kethereum.model.number

import java.math.BigInteger

actual class BigInteger(val value: BigInteger): Number(), Comparable<org.kethereum.model.number.BigInteger> {

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

    actual fun add(value: org.kethereum.model.number.BigInteger): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.add(value.value))
    }

    actual fun multiply(value: org.kethereum.model.number.BigInteger): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.multiply(value.value))
    }

    actual fun mod(value: org.kethereum.model.number.BigInteger): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.mod(value.value))
    }

    actual fun minus(value: org.kethereum.model.number.BigInteger): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.minus(value.value))
    }

    actual fun xor(value: org.kethereum.model.number.BigInteger?): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.xor(value?.value))
    }

    actual fun and(value: org.kethereum.model.number.BigInteger?): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.and(value?.value))
    }

    actual fun shiftLeft(value: Int): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.shiftLeft(value))
    }

    actual fun shiftRight(value: Int): org.kethereum.model.number.BigInteger {
        return BigInteger(this.value.shiftRight(value))
    }


    /**
     * Returns the signum function of this BigInteger.
     */
    actual fun signum(): Int {
        return this.value.signum()
    }

    override fun toString(): String {
        return this.value.toString()
    }

    actual fun toString(radix: Int): String {
        return this.value.toString(radix)
    }

    actual fun toByteArray(): ByteArray {
        return this.value.toByteArray()
    }

    actual fun byteValueExact(): Byte {
        return this.value.byteValueExact()
    }

    override fun toByte(): Byte = this.value.toByte()
    override fun toChar(): Char = this.value.toChar()
    override fun toDouble(): Double = this.value.toDouble()
    override fun toFloat(): Float = this.value.toFloat()
    override fun toInt(): Int = this.value.toInt()
    override fun toLong(): Long = this.value.toLong()
    override fun toShort(): Short = this.value.toShort()

    actual companion object {
        actual val ZERO: org.kethereum.model.number.BigInteger = org.kethereum.model.number.BigInteger(BigInteger.ZERO)
        actual val ONE: org.kethereum.model.number.BigInteger = org.kethereum.model.number.BigInteger(BigInteger.ONE)
        actual val TEN: org.kethereum.model.number.BigInteger = org.kethereum.model.number.BigInteger(BigInteger.TEN)
        actual fun valueOf(value: Long): org.kethereum.model.number.BigInteger {
            return org.kethereum.model.number.BigInteger(BigInteger.valueOf(value))
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is org.kethereum.model.number.BigInteger && this.value == other.value
    }

    override fun compareTo(other: org.kethereum.model.number.BigInteger): Int {
        return value.compareTo(other.value)
    }
}

