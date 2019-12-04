package org.kethereum.functions

import org.kethereum.model.Address
import org.komputing.khex.extensions.has0xPrefix
import org.komputing.khex.extensions.isValidHex
import org.komputing.khex.model.HexString

fun Address.isValid() = HexString(hex).let {
    it.has0xPrefix() && hex.length == 42 && it.isValidHex()
}
