package org.kethereum.contract.abi.types.model

import org.walleth.khex.hexToByteArray

open class BytesABIType(val bytes: Int) : ContractABIType {

    var value: ByteArray

    init {
        require(bytes >= 1) { "bytes must be >0" }
        require(bytes <= 32) { "bytes must be <=32" }
        value = ByteArray(bytes)
    }


    override fun toBytes() = ByteArray(32 - bytes) + value

    override fun parseValueFromString(string: String) {
        val tempValue = string.hexToByteArray()
        require(tempValue.size <= bytes) { "The given value does not fit into $bytes bytes" }
        value = tempValue
    }

    override fun isDynamic() = false

}