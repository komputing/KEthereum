package org.kethereum.abi_codegen.model

import kotlin.reflect.KClass

internal data class TypeDefinition(
        val kclass: KClass<out Any>,
        val incode: CodeWithImport,
        val outcode: CodeWithImport
)