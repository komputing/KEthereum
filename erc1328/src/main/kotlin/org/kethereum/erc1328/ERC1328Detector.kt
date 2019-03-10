package org.kethereum.erc1328

import org.kethereum.erc831.ERC831
import org.kethereum.erc831.toERC831
import org.kethereum.model.EthereumURI

fun ERC831.isERC1328() =
        scheme == "wc"

fun EthereumURI.isERC1328() = toERC831().isERC1328()
