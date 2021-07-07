package org.kethereum.methodsignatures

import org.kethereum.abi.model.EthereumFunction
import org.kethereum.abi.model.EthereumNamedType
import org.kethereum.keccakshortcut.keccak
import org.kethereum.methodsignatures.model.HexMethodSignature
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.model.Transaction
import org.komputing.khex.extensions.toNoPrefixHexString

private fun String.getHexSignature() = toByteArray().keccak().toNoPrefixHexString().substring(0, 8)

fun TextMethodSignature.toHexSignatureUnsafe() = HexMethodSignature(signature.getHexSignature())

fun TextMethodSignature.toHexSignature() = HexMethodSignature(normalizedSignature.getHexSignature())

fun Transaction.getHexSignature() = HexMethodSignature(input.slice(0..3).toNoPrefixHexString())

fun List<EthereumNamedType>.toParameterList(): String = "(" + joinToString(",") {
    if (it.type.startsWith("tuple"))
        (it.components ?: throw IllegalArgumentException("tuple must have components")).toParameterList() + it.type.removePrefix("tuple")
    else
        it.type
} + ")"

fun EthereumFunction.toTextMethodSignature() = TextMethodSignature(name + inputs.toParameterList())
fun EthereumFunction.toHexMethodSignature() = toTextMethodSignature().toHexSignature()