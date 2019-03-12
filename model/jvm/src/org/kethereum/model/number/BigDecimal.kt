package org.kethereum.model.number

import java.math.BigDecimal

/**
 *
 */
actual class BigDecimal(private val value: BigDecimal): Number(), Comparable<Number> {

    actual constructor(value: String): this(BigDecimal(value))
    actual constructor(value: BigInteger): this(value.value.toBigDecimal())

    actual operator fun times(value: org.kethereum.model.number.BigDecimal): org.kethereum.model.number.BigDecimal {
        return BigDecimal(this.value.times(value.value))
    }

    actual fun toBigInteger(): BigInteger {
        return BigInteger(this.value.toBigInteger())
    }

    override fun toByte(): Byte = this.value.toByte()
    override fun toChar(): Char = this.value.toChar()
    override fun toDouble(): Double = this.value.toDouble()
    override fun toFloat(): Float = this.value.toFloat()
    override fun toInt(): Int = this.value.toInt()
    override fun toLong(): Long = this.value.toLong()
    override fun toShort(): Short = this.value.toShort()

    override fun compareTo(other: Number): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}