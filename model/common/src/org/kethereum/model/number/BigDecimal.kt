package org.kethereum.model.number

/**
 *
 */
expect class BigDecimal: Number, Comparable<Number> {
    constructor(value: String)
    constructor(value: BigInteger)

    operator fun times(value: BigDecimal): BigDecimal

    fun toBigInteger(): BigInteger
}