package org.kethereum.model.number

/**
 *
 */
expect class BigDecimal: Number, Comparable<BigDecimal> {
    constructor(value: String)
    constructor(value: BigInteger)

    fun multiply(value: BigDecimal): BigDecimal
    fun pow(exponent: Int): BigDecimal

    fun toBigInteger(): BigInteger

    companion object {
        val TEN: BigDecimal
    }
}