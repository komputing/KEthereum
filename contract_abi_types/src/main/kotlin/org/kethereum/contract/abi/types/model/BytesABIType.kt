package org.kethereum.contract.abi.types.model

import org.walleth.khex.hexToByteArray

open class BytesABIType(val bytes: Int) : ContractABIType {

    var value: ByteArray

    init {
        if (bytes < 1) {
            throw IllegalArgumentException("bytes must be >0")
        }
        if (bytes > 32) {
            throw IllegalArgumentException("bytes must be <=32")
        }
        value = ByteArray(bytes)
    }


    override fun toBytes() = ByteArray(32 - bytes) + value

    override fun parseValueFromString(string: String) {
        val tempValue = string.hexToByteArray()
        if (tempValue.size > bytes) {
            throw IllegalArgumentException("The given value does not fit into $bytes bytes")
        }
        value = tempValue
    }

    override fun isDynamic() = false

}