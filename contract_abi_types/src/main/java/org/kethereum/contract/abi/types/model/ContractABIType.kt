package org.kethereum.contract.abi.types.model

interface ContractABIType {
    fun parseValueFromString(string: String)
    fun toBytes(): ByteArray
    fun isDynamic(): Boolean
}