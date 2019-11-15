package org.kethereum.contract.abi.types.model

const val BYTE_IN_BITS = 8
const val ETH_TYPE_PAGESIZE = 32

val TypeAliases = mapOf(
        "uint" to "uint256",
        "int" to "int256",
        "fixed" to "fixed128x18",
        "ufixed" to "ufixed128x18",
        "byte" to "bytes1"
)