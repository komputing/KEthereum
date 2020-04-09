package org.kethereum.eip1191

import org.kethereum.erc55.isValid
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.komputing.khex.extensions.toNoPrefixHexString
import java.util.*

/*
EIP-1191 Checksum as in https://github.com/ethereum/EIPs/blob/master/EIPS/eip-1191.md
 */

fun Address.withEIP1191Checksum(chainId: ChainId) = "${chainId.value}${hex}".toLowerCase(Locale.ROOT).toByteArray().keccak().toNoPrefixHexString().let { hexHash ->
    Address(cleanHex.mapIndexed { index, hexChar ->
        when {
            hexChar in '0'..'9' -> hexChar
            hexHash[index] in '0'..'7' -> hexChar.toLowerCase()
            else -> hexChar.toUpperCase()
        }
    }.joinToString(""))
}

private fun Address.hasValidEIP1191ChecksumAssumingValidAddress(chainId: ChainId) = withEIP1191Checksum(chainId).hex == hex

fun Address.hasValidEIP1191Checksum(chainId: ChainId) = isValid() && hasValidEIP1191ChecksumAssumingValidAddress(chainId)
fun Address.hasValidEIP1191ChecksumOrNoChecksum(chainId: ChainId) = isValid() &&
        (hasValidEIP1191ChecksumAssumingValidAddress(chainId) ||
                cleanHex.toLowerCase() == cleanHex ||
                cleanHex.toUpperCase() == cleanHex)

