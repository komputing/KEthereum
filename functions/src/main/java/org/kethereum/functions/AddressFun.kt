package org.kethereum.functions

import org.kethereum.model.Address
import org.walleth.khex.HEX_REGEX


fun Address.isValid() = hex.startsWith("0x") && hex.length == 42 && HEX_REGEX.matches(hex)
