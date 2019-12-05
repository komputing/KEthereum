package org.kethereum.abi_codegen

import com.squareup.kotlinpoet.*
import org.kethereum.abi.EthereumABI
import org.kethereum.abi.getAllFunctions
import org.kethereum.abi_codegen.model.ADDRESS_FIELD_NAME
import org.kethereum.abi_codegen.model.GeneratorSpec
import org.kethereum.abi_codegen.model.RPC_FIELD_NAME
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.convertStringToABITypeOrNull
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.rpc.EthereumRPC

fun EthereumABI.toKotlinCode(spec: GeneratorSpec): FileSpec {

    val fileSpec = FileSpec.builder(spec.packageName, spec.classPrefix)
    val transactionDetector = spec.txDecoderName?.let {
        TypeSpec.classBuilder(it).defaultConstructor(emptyList())
    }

    val transactionsClassBuilder = TypeSpec.classBuilder(spec.classPrefix + "TransactionGenerator")
            .defaultConstructor(listOf(ParameterSpec.builder(ADDRESS_FIELD_NAME, Address::class).build()))

    val rpcClassBuilder = spec.rpcConnectorName?.let {
        TypeSpec.classBuilder(it).defaultConstructor(listOf(
                ParameterSpec.builder(ADDRESS_FIELD_NAME, Address::class).build(),
                ParameterSpec.builder(RPC_FIELD_NAME, EthereumRPC::class).build()))
    }

    val allClasses = listOf(transactionsClassBuilder, rpcClassBuilder, transactionDetector)

    if (spec.internal) allClasses.forEach {
        it?.modifiers?.add(KModifier.INTERNAL)
    }

    rpcClassBuilder?.addProperty(PropertySpec.builder(RPC_FIELD_NAME, EthereumRPC::class).addModifiers(KModifier.PRIVATE).initializer(RPC_FIELD_NAME).build())
    rpcClassBuilder?.addProperty(PropertySpec.builder(ADDRESS_FIELD_NAME, Address::class).addModifiers(KModifier.PRIVATE).initializer(ADDRESS_FIELD_NAME).build())

    val createEmptyTX = MemberName("org.kethereum.model", "createEmptyTransaction")
    val encodeTypes = MemberName("org.kethereum.contract.abi.types", "encodeTypes")

    val tx = PropertySpec.builder("tx", Transaction::class).addModifiers(KModifier.PRIVATE).initializer("%M().apply { to = address }", createEmptyTX).build()
    transactionsClassBuilder.addProperty(tx)

    val generatorType = ClassName(spec.packageName, spec.classPrefix + "TransactionGenerator")
    val txGenerator = PropertySpec.builder("txGenerator", generatorType)
            .addModifiers(KModifier.PRIVATE).initializer("%T(address)", generatorType).build()

    rpcClassBuilder?.addProperty(txGenerator)

    methodList.getAllFunctions().createModel().forEach { it ->

        val kotlinTypesFunBuilder = FunSpec.builder(it.functionName)
        val ethTypeFunBuilder = FunSpec.builder(it.ethTypesFunctionName).addModifiers(KModifier.PRIVATE)

        val kotlinTxTypesFunBuilder = FunSpec.builder(it.functionName)
        val ethTypeTxFunBuilder = FunSpec.builder(it.ethTypesFunctionName).addModifiers(KModifier.INTERNAL)

        kotlinTypesFunBuilder.addKdoc(it.kDoc)
        kotlinTxTypesFunBuilder.addKdoc(it.kDoc)

        it.params.forEach { param ->

            if (param.typeDefinition != null) {
                ethTypeFunBuilder.addParameter(param.parameterName, param.typeDefinition.ethTypeKClass)
                ethTypeTxFunBuilder.addParameter(param.parameterName, param.typeDefinition.ethTypeKClass)

                kotlinTypesFunBuilder.addParameter(param.parameterName, param.typeDefinition.kotlinTypeKClass)
                kotlinTxTypesFunBuilder.addParameter(param.parameterName, param.typeDefinition.kotlinTypeKClass)

            } else {
                it.skipReason = "${it.textMethodSignature.signature} contains unsupported parameter type ${param.type} for ${it.functionName}"
            }
        }

        kotlinTypesFunBuilder.addParameter(ParameterSpec.builder("blockSpec", String::class).defaultValue("\"latest\"").build())
        ethTypeFunBuilder.addParameter(ParameterSpec.builder("blockSpec", String::class).defaultValue("\"latest\"").build())

        fileSpec.addProperty(PropertySpec.builder(it.fourByteName, ByteArray::class).initializer(it.signatureCode).build())

        transactionDetector?.addFunction(FunSpec.builder("is" + it.maybeExtendedFunctionName)
                .addParameter("tx", Transaction::class)
                .addStatement("return tx.input.sliceArray(0..3).contentEquals(${it.fourByteName})")
                .returns(Boolean::class).build())

        ethTypeTxFunBuilder.addCode("return ${tx.name}.copy(input = ${it.fourByteName} + %M(${it.params.joinToString { it.parameterName }}))", encodeTypes)

        ethTypeFunBuilder.addCode("val tx = ${txGenerator.name}.${it.ethTypesFunctionName}(${it.params.joinToString { it.parameterName }})\n")
        val rpcCall = """rpc.call(tx, blockSpec)"""

        val ethTypeCallProto = "${it.ethTypesFunctionName}(${it.params.joinToString { "%T.ofNativeKotlinType(${it.parameterName}${it.typeDefinition?.params.toParamIfExist()})" }}"

        val callTypeProtoWithBlockSpec = ethTypeCallProto + (if (it.params.isNotEmpty()) "," else "") + "blockSpec"

        kotlinTxTypesFunBuilder.returns(Transaction::class)
        kotlinTxTypesFunBuilder.addCode("return $ethTypeCallProto)\n", *it.ethTypeArray)
        when {
            it.outputs.size > 1 -> it.skipReason = "${it.textMethodSignature.signature} has more than one output - which is currently not supported"
            it.outputs.size == 1 -> {
                val type = it.outputs.first().type

                val typeDefinition = convertStringToABITypeOrNull(type)
                if (typeDefinition != null) {
                    ethTypeFunBuilder.returns(typeDefinition.ethTypeKClass.asTypeName().copy(nullable = true))
                    kotlinTypesFunBuilder.returns(typeDefinition.kotlinTypeKClass.asTypeName().copy(nullable = true))

                    ethTypeFunBuilder.addStatement(
                            "return %T.ofPaginatedByteArray(%T($rpcCall)${typeDefinition.params.toParamIfExist()})",
                            typeDefinition.ethTypeKClass, PaginatedByteArray::class
                    )

                    kotlinTypesFunBuilder.addCode("return $callTypeProtoWithBlockSpec)?.toKotlinType()\n", *it.ethTypeArray)
                } else {
                    it.skipReason = "${it.textMethodSignature.signature} has unsupported returntype: $type"
                }
            }
            else -> {
                ethTypeFunBuilder.addStatement(rpcCall)
                kotlinTypesFunBuilder.addCode("$callTypeProtoWithBlockSpec)", *it.ethTypeArray)
            }
        }

        transactionsClassBuilder.addFunction(ethTypeTxFunBuilder.build())
        transactionsClassBuilder.addFunction(kotlinTxTypesFunBuilder.build())

        if (it.skipReason != null) {
            rpcClassBuilder?.addFunction(ethTypeFunBuilder.build())
            rpcClassBuilder?.addFunction(kotlinTypesFunBuilder.build())
        } else {
            rpcClassBuilder?.addKdoc("\nskipped function $it " + it.skipReason)
        }
    }

    fileSpec.addImport("org.kethereum.contract.abi.types.model.type_params", "BytesTypeParams", "BitsTypeParams")
    allClasses.filterNotNull().forEach { fileSpec.addType(it.build()) }
    return fileSpec.build()
}