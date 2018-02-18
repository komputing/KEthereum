package org.kethereum.erc55

import org.kethereum.functions.isValid
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.walleth.khex.fromHexToInt
import org.walleth.khex.toHexString

/*
ERC-55 Checksum as in https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md
 */

fun Address.withERC55Checksum() = cleanHex.toLowerCase().toByteArray().keccak().toHexString("").let {
    Address(cleanHex.mapIndexed { index, c ->
        when {
            c in '0'..'9' -> c
            it[index].toLowerCase().fromHexToInt() >= 8 -> c.toUpperCase()
            else -> c.toLowerCase()
        }
    }.joinToString(""))
}

private fun Address.hasValidEIP55ChecksumStrictAssumingValidAddress() = withERC55Checksum().hex == hex

fun Address.hasValidEIP55Checksum() = isValid() && hasValidEIP55ChecksumStrictAssumingValidAddress()
fun Address.hasValidEIP55ChecksumOrNoCecksum() = isValid() &&
        (hasValidEIP55ChecksumStrictAssumingValidAddress() ||
                cleanHex.toLowerCase() == cleanHex ||
                cleanHex.toUpperCase() == cleanHex)
