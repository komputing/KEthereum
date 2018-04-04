package org.kethereum.contract.abi.types.model

class BoolABIType : IntABIType(8, false) {

    override fun parseValueFromString(string: String) = when (string.toLowerCase()) {
        "true" -> super.parseValueFromString("1")
        "false" -> super.parseValueFromString("0")
        else -> throw IllegalArgumentException("boolean must be true or false")
    }
}