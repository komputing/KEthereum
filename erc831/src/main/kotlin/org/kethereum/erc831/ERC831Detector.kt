package org.kethereum.erc831

fun String.isEthereumURLString() = startsWith("ethereum:")
fun ERC831.isERC831() = scheme == "ethereum"
