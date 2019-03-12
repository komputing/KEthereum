package org.kethereum.contract.abi.types.model

import org.kethereum.model.extensions.toBytesPadded
import org.kethereum.model.number.BigInteger

open class IntABIType(val bits: Int, val signed: Boolean) : ContractABIType {

    private var value: BigInteger = BigInteger.ZERO

    override fun toBytes() = value.toBytesPadded(32)

    override fun parseValueFromString(string: String) {
        value = BigInteger(string)

        if (!signed && value < BigInteger.ZERO) {
            throw IllegalArgumentException("value cannot be less than 0 for unsigned int")
        }
    }

    override fun isDynamic() = false
}