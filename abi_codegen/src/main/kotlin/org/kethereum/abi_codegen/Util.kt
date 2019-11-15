package org.kethereum.abi_codegen

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import org.kethereum.contract.abi.types.model.ETHTypeParams

internal fun String.toByteArrayOfCode() = "byteArrayOf(" + (0 until length / 2).joinToString() { pos ->
    substring(pos * 2, pos * 2 + 2).toInt(16).toByte().toString()
} + ")"

internal fun ETHTypeParams?.toParamIfExist() = if (this == null) "" else ",\"" + encodeToString() + '"'

internal fun TypeSpec.Builder.defaultConstructor(paramSpecs: Iterable<ParameterSpec>) = primaryConstructor(FunSpec.constructorBuilder()
        .addParameters(paramSpecs)
        .build())
