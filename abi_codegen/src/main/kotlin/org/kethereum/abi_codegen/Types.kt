package org.kethereum.abi_codegen

import org.kethereum.abi_codegen.model.CodeWithImport
import org.kethereum.abi_codegen.model.REPLACEMENT_TOKEN
import org.kethereum.abi_codegen.model.TypeDefinition
import org.kethereum.model.Address
import org.kethereum.type_aliases.TypeAliases

internal val ByteTypeDefinition = TypeDefinition(
        ByteArray::class,
        CodeWithImport("$REPLACEMENT_TOKEN.toFixedLengthByteArray(32)", listOf("org.kethereum.extensions.toFixedLengthByteArray")),
        CodeWithImport("$REPLACEMENT_TOKEN.hexToByteArray()", listOf("org.walleth.khex.hexToByteArray")))

internal fun getType(string: String) = typeMap[TypeAliases.getOrDefault(string, string)]

internal val typeMap by lazy {
    mutableMapOf(
            "address" to TypeDefinition(
                    Address::class,
                    CodeWithImport("$REPLACEMENT_TOKEN.hex.hexToByteArray().toFixedLengthByteArray(32)", listOf("org.walleth.khex.hexToByteArray", "org.kethereum.extensions.toFixedLengthByteArray")),
                    CodeWithImport("Address($REPLACEMENT_TOKEN.substring(24, 64))")),
            "bool" to TypeDefinition(
                    Boolean::class,
                    CodeWithImport("ByteArray(32) {if ($REPLACEMENT_TOKEN && it==31) 1 else 0}"),
                    CodeWithImport("$REPLACEMENT_TOKEN.replace(\"0\",\"\").isNotEmpty()"))
    ).apply {
        (1..32).forEach {
            this["bytes$it"] = ByteTypeDefinition
        }
    }
}