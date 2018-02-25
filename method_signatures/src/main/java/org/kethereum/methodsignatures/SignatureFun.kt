package org.kethereum.methodsignatures

import org.kethereum.keccakshortcut.keccak
import org.walleth.khex.toNoPrefixHexString

data class TextMethodSignature(val signature: String)
data class HexMethodSignature(val hex: String)

private fun String.getHexSignature() = toByteArray().keccak().toNoPrefixHexString().substring(0, 8)

fun TextMethodSignature.toHexSignature() = HexMethodSignature(signature.getHexSignature())
