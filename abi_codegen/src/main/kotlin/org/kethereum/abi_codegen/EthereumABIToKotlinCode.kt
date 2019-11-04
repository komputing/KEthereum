package org.kethereum.abi_codegen

import com.squareup.kotlinpoet.*
import org.kethereum.abi.EthereumABI
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.methodsignatures.toTextMethodSignature
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC

fun EthereumABI.toKotlinCode(className: String, packageName: String = ""): FileSpec {

    val classBuilder = TypeSpec.classBuilder(className)
            .primaryConstructor(FunSpec.constructorBuilder()
                    .addParameter("rpc", EthereumRPC::class)
                    .addParameter("address", Address::class)
                    .build())

    classBuilder.addProperty(PropertySpec.builder("rpc", EthereumRPC::class).addModifiers(KModifier.PRIVATE).initializer("rpc").build())
    classBuilder.addProperty(PropertySpec.builder("address", Address::class).addModifiers(KModifier.PRIVATE).initializer("address").build())

    val createEmptyTX = MemberName("org.kethereum.model", "createEmptyTransaction")

    methodList.filter { it.type == "function" }.forEach { it ->

        val funBuilder = FunSpec.builder(it.name!!)

        val textMethodSignature = it.toTextMethodSignature()
        val fourByteSignature = textMethodSignature.toHexSignature().hex
        val signatureCode = fourByteSignature.toByteArrayOfCode()

        funBuilder.addKdoc("Signature: " + textMethodSignature.signature)
        funBuilder.addKdoc("\n4Byte: $fourByteSignature")
        funBuilder.addCode("""
            |val tx = %M().apply {
            |    to = address
            |    input = $signatureCode
            |}
            |val result = rpc.call(tx, "latest")?.result?.removePrefix("0x")
            |
                """.trimMargin(), createEmptyTX)
        val outputCount = it.outputs?.size ?: 0
        if (outputCount > 1) {
            funBuilder.addKdoc("!!Warning!! this function has more than one output - which is currently not supported")
        } else if (outputCount == 1) {
            when (it.outputs!!.first().type) {
                "address" -> {
                    funBuilder.returns(Address::class.asTypeName().copy(nullable = true))
                    funBuilder.addStatement("return result?.let { Address(result.substring(24, 64)) }")
                }
            }

        }

        classBuilder.addFunction(funBuilder.build())
    }

    return FileSpec.builder(packageName, className)
            .addType(classBuilder.build())
            .build()
}