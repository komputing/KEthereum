package org.kethereum

import com.ionspin.kotlin.bignum.integer.BigInteger


val ETH_IN_WEI = BigInteger.parseString("1000000000000000000")

var DEFAULT_GAS_PRICE = BigInteger.parseString("20000000000")
var DEFAULT_GAS_LIMIT = BigInteger.parseString("21000")

const val DEFAULT_ETHEREUM_BIP44_PATH = "m/44'/60'/0'/0/0"