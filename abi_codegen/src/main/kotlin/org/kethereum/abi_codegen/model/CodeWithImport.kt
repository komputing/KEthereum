package org.kethereum.abi_codegen.model

internal data class CodeWithImport(
        val code: String,
        val imports: List<String> = emptyList()
)