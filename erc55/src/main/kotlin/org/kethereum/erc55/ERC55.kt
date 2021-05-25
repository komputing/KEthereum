package org.kethereum.erc55

import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.komputing.khex.extensions.toNoPrefixHexString
import java.util.*

/*
ERC-55 Checksum as in https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md
 */

fun Address.withERC55Checksum() = cleanHex.lowercase().toByteArray().keccak().toNoPrefixHexString().let { hexHash ->
    Address(cleanHex.mapIndexed { index, hexChar ->
        when {
            hexChar in '0'..'9' -> hexChar
            hexHash[index] in '0'..'7' -> hexChar.lowercase()
            else -> hexChar.uppercase()
        }
    }.joinToString(""))
}

private fun Address.hasValidERC55ChecksumAssumingValidAddress() = withERC55Checksum().hex == hex

fun Address.hasValidERC55Checksum() = isValid() && hasValidERC55ChecksumAssumingValidAddress()
fun Address.hasValidERC55ChecksumOrNoChecksum() = isValid() &&
        (hasValidERC55ChecksumAssumingValidAddress() ||
                cleanHex.lowercase() == cleanHex ||
                cleanHex.uppercase() == cleanHex)
