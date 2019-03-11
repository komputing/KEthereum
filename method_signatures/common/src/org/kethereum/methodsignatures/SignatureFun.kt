package org.kethereum.methodsignatures

import org.kethereum.keccakshortcut.keccak
import org.kethereum.methodsignatures.model.HexMethodSignature
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.walleth.khex.toNoPrefixHexString

private fun String.getHexSignature() = toByteArray().keccak().toNoPrefixHexString().substring(0, 8)

fun TextMethodSignature.toHexSignatureUnsafe() = HexMethodSignature(signature.getHexSignature())

fun TextMethodSignature.toHexSignature() = HexMethodSignature(normalizedSignature.getHexSignature())
