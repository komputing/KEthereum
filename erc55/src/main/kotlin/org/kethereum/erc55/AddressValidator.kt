package org.kethereum.erc55

import org.kethereum.model.Address
import org.komputing.khex.extensions.has0xPrefix
import org.komputing.khex.extensions.isValidHex
import org.komputing.khex.model.HexString

/**
 * checks if the address has the correct length, prefix and is valid hex
 */
fun Address.isValid() = HexString(hex).let {
    it.has0xPrefix() && hex.length == 42 && it.isValidHex()
}
