package org.kethereum.functions

import org.kethereum.model.Address
import org.walleth.khex.HEX_REGEX
import org.walleth.khex.has0xPrefix


fun Address.isValid() = hex.has0xPrefix() && hex.length == 42 && HEX_REGEX.matches(hex)
