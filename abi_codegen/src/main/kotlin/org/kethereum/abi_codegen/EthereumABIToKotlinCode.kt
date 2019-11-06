package org.kethereum.abi_codegen

import com.squareup.kotlinpoet.*
import org.kethereum.abi.EthereumABI
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.methodsignatures.toTextMethodSignature
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC
import kotlin.reflect.KClass


data class CodeWithImport(
        val code: String,
        val imports: List<String> = emptyList()
)

data class TypeDefinition(
        val kclass: KClass<out Any>,
        val incode: CodeWithImport,
        val outcode: CodeWithImport
)

val typeMap = mapOf(
        "address" to TypeDefinition(
                Address::class,
                CodeWithImport(".hex.hexToByteArray().toFixedLengthByteArray(32)", listOf("org.walleth.khex.hexToByteArray", "org.kethereum.extensions.toFixedLengthByteArray")),
                CodeWithImport("Address(%%HEX%%.substring(24, 64))")),
        "bytes32" to TypeDefinition(
                ByteArray::class,
                CodeWithImport(".toFixedLengthByteArray(32)", listOf("org.kethereum.extensions.toFixedLengthByteArray")),
                CodeWithImport("%%HEX%%.hexToByteArray()", listOf("org.walleth.khex.hexToByteArray"))),
        "bool" to TypeDefinition(
                Boolean::class,
                CodeWithImport(".let { b -> ByteArray(32,{if (b && it==31) 1 else 0})}"),
                CodeWithImport("%%HEX%%.replace(\"0\",\"\").isNotEmpty()"))
)

fun EthereumABI.toKotlinCode(className: String, packageName: String = ""): FileSpec {

    val classBuilder = TypeSpec.classBuilder(className)
            .primaryConstructor(FunSpec.constructorBuilder()
                    .addParameter("rpc", EthereumRPC::class)
                    .addParameter("address", Address::class)
                    .build())

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

        it.inputs?.forEach {
            val typeDefinition = typeMap[it.type]
            if (typeDefinition != null) {
                funBuilder.addParameter(it.name, typeDefinition.kclass)
                inputCodeList.add(it.name + typeDefinition.incode.code)
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

            val typeDefinition = typeMap[type]
            if (typeDefinition != null) {
                imports.addAll(typeDefinition.outcode.imports)
                funBuilder.returns(typeDefinition.kclass.asTypeName().copy(nullable = true))
                funBuilder.addStatement("return result?.let {" + typeDefinition.outcode.code.replace("%%HEX%%", "result") + "}")
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