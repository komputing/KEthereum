package org.kethereum.contract.abi.types.model

class DynamicSizedBytesABIType() : ContractABIType {

    override fun isDynamic() = true

    override fun toBytes() = TODO()

    override fun parseValueFromString(string: String) = TODO()

}