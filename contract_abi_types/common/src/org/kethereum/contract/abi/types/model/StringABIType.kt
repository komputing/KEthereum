package org.kethereum.contract.abi.types.model

class StringABIType : ContractABIType {

    override fun isDynamic() = true

    override fun toBytes() = TODO()

    override fun parseValueFromString(string: String) = TODO()
}