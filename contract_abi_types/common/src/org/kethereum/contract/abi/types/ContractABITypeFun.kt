package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.*

// as defined in https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI


val BYTES_COUNT_CONSTRAINT: (Int) -> Unit = {
    if (it < 1) throw  IllegalArgumentException("bytes count MUST be more than 0")
    if (it > 32) throw  IllegalArgumentException("bytes count MUST be less than 20")
}

val INT_BITS_CONSTRAINT: (Int) -> Unit = {
    if (it % BYTE_IN_BITS > 0) {
        throw IllegalArgumentException("bits%$BYTE_IN_BITS MUST be 0 but ist not for $it")
    }

    if (it < 8) {
        throw IllegalArgumentException("cannot have less than 8 bit")
    }

    if (it > 256) {
        throw IllegalArgumentException("cannot have more than 256 bits")
    }
}


fun convertStringToABIType(string: String) = when {
    string == "bool" -> BoolABIType()
    string == "string" -> StringABIType()
    string == "address" -> AddressABIType()
    string == "bytes" -> DynamicSizedBytesABIType()

    string.startsWith("int") -> IntABIType(string.extractPrefixedNumber("int", INT_BITS_CONSTRAINT), true)
    string.startsWith("uint") -> IntABIType(string.extractPrefixedNumber("uint", INT_BITS_CONSTRAINT), false)
    string.startsWith("bytes") -> BytesABIType(string.extractPrefixedNumber("bytes", BYTES_COUNT_CONSTRAINT))

    else -> throw IllegalArgumentException("$string is not a supported type")
}

private fun String.extractPrefixedNumber(prefix: String, constraint: (Int) -> Unit) =
        (removePrefix(prefix)
                .toIntOrNull() ?: throw IllegalArgumentException("$this MUST have only a number after $prefix"))
                .also(constraint)
