package org.kethereum.abi_codegen

import com.squareup.kotlinpoet.*
import org.kethereum.abi.EthereumABI
import org.kethereum.abi_codegen.model.CodeWithImport
import org.kethereum.abi_codegen.model.REPLACEMENT_TOKEN
import org.kethereum.abi_codegen.model.TypeDefinition
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.methodsignatures.toTextMethodSignature
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC
import org.kethereum.type_aliases.TypeAliases

internal val ByteTypeDefinition = TypeDefinition(
        ByteArray::class,
        CodeWithImport("$REPLACEMENT_TOKEN.toFixedLengthByteArray(32)", listOf("org.kethereum.extensions.toFixedLengthByteArray")),
        CodeWithImport("$REPLACEMENT_TOKEN.hexToByteArray()", listOf("org.walleth.khex.hexToByteArray")))

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
            this["byte$it"] = ByteTypeDefinition
        }
    }
}

internal fun getType(string: String) = typeMap[TypeAliases.getOrDefault(string, string)]

fun EthereumABI.toKotlinCode(className: String,
                             packageName: String = "",
                             internal: Boolean = true): FileSpec {

    val classBuilder = TypeSpec.classBuilder(className)
            .primaryConstructor(FunSpec.constructorBuilder()
                    .addParameter("rpc", EthereumRPC::class)
                    .addParameter("address", Address::class)
                    .build())

    if (internal) {
        classBuilder.modifiers.add(KModifier.INTERNAL)
    }

    classBuilder.addProperty(PropertySpec.builder("rpc", EthereumRPC::class).addModifiers(KModifier.PRIVATE).initializer("rpc").build())
    classBuilder.addProperty(PropertySpec.builder("address", Address::class).addModifiers(KModifier.PRIVATE).initializer("address").build())

    val createEmptyTX = MemberName("org.kethereum.model", "createEmptyTransaction")
    val imports = mutableSetOf<String>()

    val skippedFunctions = mutableMapOf<String, String>()

    methodList.filter { it.type == "function" }.forEach { it ->

        val funBuilder = FunSpec.builder(it.name!!)

        val textMethodSignature = it.toTextMethodSignature()
        val fourByteSignature = textMethodSignature.toHexSignature().hex
        val signatureCode = fourByteSignature.toByteArrayOfCode()

        funBuilder.addKdoc("Signature: " + textMethodSignature.signature)
        funBuilder.addKdoc("\n4Byte: $fourByteSignature")

        val inputCodeList = mutableListOf(signatureCode)

        var blankParamCounter = -1
        it.inputs?.forEach {
            val typeDefinition = getType(it.type)

            val parameterName = if (it.name.isBlank()) {
                blankParamCounter++
                "parameter$blankParamCounter"
            } else {
                it.name
            }
            if (typeDefinition != null) {
                funBuilder.addParameter(parameterName, typeDefinition.kclass)
                inputCodeList.add(typeDefinition.incode.code.replace(REPLACEMENT_TOKEN, parameterName))
                imports.addAll(typeDefinition.incode.imports)
            } else {
                skippedFunctions[fourByteSignature] = "${textMethodSignature.signature} contains unsupported parameter type ${it.type} for ${it.name}"
            }

        }

        val input = inputCodeList.joinToString(" + ")
        funBuilder.addCode("""
            |val tx = %M().apply {
            |    to = address
            |    input = $input
            |}
            |val result = rpc.call(tx, "latest")?.result?.removePrefix("0x")
            |
                """.trimMargin(), createEmptyTX)
        val outputCount = it.outputs?.size ?: 0
        if (outputCount > 1) {
            skippedFunctions[fourByteSignature] = "${textMethodSignature.signature} has more than one output - which is currently not supported"
        } else if (outputCount == 1) {
            val type = it.outputs!!.first().type

            val typeDefinition = getType(type)
            if (typeDefinition != null) {
                imports.addAll(typeDefinition.outcode.imports)
                funBuilder.returns(typeDefinition.kclass.asTypeName().copy(nullable = true))
                funBuilder.addStatement("return result?.let {" + typeDefinition.outcode.code.replace(REPLACEMENT_TOKEN, "result") + "}")
            } else {
                skippedFunctions[fourByteSignature] = "${textMethodSignature.signature} has unsupported returntype: $type"
            }
        }

        if (!skippedFunctions.containsKey(fourByteSignature)) {
            classBuilder.addFunction(funBuilder.build())
        }
    }

    skippedFunctions.keys.forEach {
        classBuilder.addKdoc("\nskipped function $it " + skippedFunctions[it])
    }
    val fileSpec = FileSpec.builder(packageName, className)
            .addType(classBuilder.build())
    imports.forEach {
        fileSpec.addImport(it.substringBeforeLast("."), it.substringAfterLast("."))
    }
    return fileSpec.build()
}