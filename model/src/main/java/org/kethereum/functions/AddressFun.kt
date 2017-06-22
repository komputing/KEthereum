package org.kethereum.functions

import org.kethereum.model.Address


fun Address.isValid() = hex.startsWith("0x") && hex.length == 42
