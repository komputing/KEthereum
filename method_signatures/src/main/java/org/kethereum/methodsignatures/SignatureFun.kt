package org.kethereum.methodsignatures

import org.kethereum.keccakshortcut.keccak
import org.kethereum.methodsignatures.model.HexMethodSignature
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.walleth.khex.toNoPrefixHexString

private fun String.getHexSignature() = toByteArray().keccak().toNoPrefixHexString().substring(0, 8)

// not with all synonyms the synonym is used for calculating the signature - e.g. address is not replaced
// see https://solidity.readthedocs.io/en/develop/abi-spec.html
private fun String.replaceParameterSynonyms() : String {
    val parameters = this.substringAfter("(").substringBefore(")")
    val replacedParameters = parameters.split(",").joinToString(",") {
        when (it) {
            "uint" -> "uint256"
            "int" -> "int256"
            "fixed" -> "fixed128x18"
            "ufixed" -> "ufixed128x18"
            else -> it
        }
    }

    val functionName = substringBefore("(")
    return "$functionName($replacedParameters)"
}

fun TextMethodSignature.toHexSignatureUnsafe() = HexMethodSignature(signature.getHexSignature())

fun TextMethodSignature.toHexSignature() = HexMethodSignature(signature.replaceParameterSynonyms().getHexSignature())
