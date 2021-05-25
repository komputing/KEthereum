package org.kethereum.abi_codegen.model

import org.kethereum.abi_codegen.toByteArrayOfCode
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import kotlin.reflect.KClass

class Function(
        val functionName: String,
        val ethTypesFunctionName: String = functionName + "ETHTyped",
        val textMethodSignature: TextMethodSignature,
        private val fourByteSignature: String = textMethodSignature.toHexSignature().hex,
        val signatureCode: String = fourByteSignature.toByteArrayOfCode(),
        val kDoc: String = "Signature: " + textMethodSignature.signature + "\n4Byte: $fourByteSignature",
        val outputs: List<Output>,
        val params: List<Params>,
        var skipReason: String? = "",
        var nameUsedMoreThanOnce: Boolean,
        val maybeExtendedFunctionName: String = functionName.replaceFirstChar { char -> char.uppercase() } + if (nameUsedMoreThanOnce) params.joinToString("") { it.parameterName.replaceFirstChar { char -> char.uppercase() } } else "",
        val fourByteName: String = "FourByte$maybeExtendedFunctionName",
        val ethTypeArray: Array<KClass<out ETHType<*>>?> = params.map { it.typeDefinition?.ethTypeKClass }.toTypedArray()
)