package org.kethereum.erc55

import org.kethereum.functions.isValid
import org.kethereum.model.Address
import org.walleth.keccak_shortcut.keccak
import org.walleth.khex.fromHexToInt

/*
ERC-55 Checksum as in https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md
 */

fun Address.withERC55Checksum() = cleanHex.toLowerCase().keccak().toCharArray().let {
    Address(cleanHex.mapIndexed { index, c ->
        if (c in '0'..'9')
            c
        else
            if (it[index].toLowerCase().fromHexToInt() >= 8) c.toUpperCase() else c.toLowerCase()
    }.joinToString(""))
}

private fun Address.hasValidEIP55ChecksumStrictAssumingValidAddress() = withERC55Checksum().hex == this.hex

fun Address.hasValidEIP55ChecksumStrict() = isValid() && hasValidEIP55ChecksumStrictAssumingValidAddress()
fun Address.hasValidEIP55Checksum() = isValid() &&
        (hasValidEIP55ChecksumStrictAssumingValidAddress() ||
                cleanHex.toLowerCase() == cleanHex ||
                cleanHex.toUpperCase() == cleanHex)
