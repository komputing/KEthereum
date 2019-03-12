package org.kethereum.functions

import org.kethereum.model.Address
import org.kethereum.model.HEX_REGEX
import org.kethereum.model.extensions.has0xPrefix


fun Address.isValid() = hex.has0xPrefix() && hex.length == 42 && HEX_REGEX.matches(hex)
