package org.kethereum.contract.abi.types

val allETHTypes by lazy {
    (1..32).map { "bytes$it" } +
            (8..256 step 8).map { "int$it" } +
            (8..256 step 8).map { "uint$it" } +
            listOf("bool", "string", "address", "bytes")
}
