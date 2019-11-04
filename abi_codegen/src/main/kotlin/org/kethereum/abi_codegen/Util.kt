package org.kethereum.abi_codegen

fun String.toByteArrayOfCode() = "byteArrayOf(" + (0 until length / 2).joinToString(",") { pos ->
    substring(pos * 2, pos * 2 + 2).toInt(16).toByte().toString()
} + ")"