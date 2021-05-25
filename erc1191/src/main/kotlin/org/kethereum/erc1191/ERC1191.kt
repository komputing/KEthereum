package org.kethereum.erc1191

import org.kethereum.erc55.isValid
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.komputing.khex.extensions.toNoPrefixHexString
import java.util.*

/*
EIP-1191 Checksum as in https://github.com/ethereum/EIPs/blob/master/EIPS/eip-1191.md
 */

fun Address.withERC1191Checksum(chainId: ChainId) = "${chainId.value}${hex}".lowercase().toByteArray().keccak().toNoPrefixHexString().let { hexHash ->
    Address(cleanHex.mapIndexed { index, hexChar ->
        when {
            hexChar in '0'..'9' -> hexChar
            hexHash[index] in '0'..'7' -> hexChar.lowercaseChar()
            else -> hexChar.uppercaseChar()
        }
    }.joinToString(""))
}

private fun Address.hasValidERC1191ChecksumAssumingValidAddress(chainId: ChainId) = withERC1191Checksum(chainId).hex == hex

fun Address.hasValidERC1191Checksum(chainId: ChainId) = isValid() && hasValidERC1191ChecksumAssumingValidAddress(chainId)
fun Address.hasValidERC1191ChecksumOrNoChecksum(chainId: ChainId) = isValid() &&
        (hasValidERC1191ChecksumAssumingValidAddress(chainId) ||
                cleanHex.lowercase() == cleanHex ||
                cleanHex.uppercase() == cleanHex)

