package org.kethereum.erc55

import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.komputing.khex.extensions.toNoPrefixHexString
import java.util.*

/*
ERC-55 Checksum as in https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md
https://github.com/ethereum/EIPs/blob/master/EIPS/eip-1191.md
 */

private fun Address.cleanHexWithPrefix(chainId: ChainId?) =
        if (chainId != null) {
            "${chainId.value}0x${cleanHex}"
        } else {
            cleanHex
        }

fun Address.withERC55Checksum(chainId: ChainId? = null) = cleanHexWithPrefix(chainId).toLowerCase(Locale.ROOT).toByteArray().keccak().toNoPrefixHexString().let { hexHash ->
    Address(cleanHex.mapIndexed { index, hexChar ->
        when {
            hexChar in '0'..'9' -> hexChar
            hexHash[index] in '0'..'7' -> hexChar.toLowerCase()
            else -> hexChar.toUpperCase()
        }
    }.joinToString(""))
}

private fun Address.hasValidERC55ChecksumAssumingValidAddress(chainId: ChainId? = null) = withERC55Checksum(chainId).hex == hex

fun Address.hasValidERC55Checksum(chainId: ChainId? = null) = isValid() && hasValidERC55ChecksumAssumingValidAddress(chainId)
fun Address.hasValidERC55ChecksumOrNoChecksum(chainId: ChainId? = null) = isValid() &&
        (hasValidERC55ChecksumAssumingValidAddress(chainId) ||
                cleanHex.toLowerCase() == cleanHex ||
                cleanHex.toUpperCase() == cleanHex)
