package org.kethereum.model

import org.kethereum.model.number.BigInteger

val ETH_IN_WEI = BigInteger("1000000000000000000")

var DEFAULT_GAS_PRICE = BigInteger("20000000000")
var DEFAULT_GAS_LIMIT = BigInteger("21000")

val HEX_REGEX = Regex("0[xX][0-9a-fA-F]+")

const val DEFAULT_ETHEREUM_BIP44_PATH = "m/44'/60'/0'/0/0"