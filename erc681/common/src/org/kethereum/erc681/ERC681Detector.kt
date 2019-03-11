package org.kethereum.erc681

import org.kethereum.erc831.ERC831
import org.kethereum.erc831.toERC831
import org.kethereum.model.EthereumURI

fun ERC831.isERC681() = scheme == "ethereum" && (prefix == null || prefix == "pay")
fun EthereumURI.isERC681() = toERC831().isERC681()
