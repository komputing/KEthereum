package org.kethereum.erc1328

import org.kethereum.model.EthereumURI
import org.kethereum.uri.common.CommonEthereumURIData
import org.kethereum.uri.common.parseCommonURI

fun CommonEthereumURIData.isERC1328() =
        scheme == "ethereum" && prefix == ERC1328_PREFIX

fun EthereumURI.isERC1328() = parseCommonURI().isERC1328()
