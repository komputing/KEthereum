package org.kethereum.erc681

import org.kethereum.model.EthereumURI
import org.kethereum.uri.common.CommonEthereumURIData
import org.kethereum.uri.common.parseCommonURI

fun CommonEthereumURIData.isERC681() = scheme == "ethereum" && (prefix == null || prefix == "pay")
fun EthereumURI.isERC681() = parseCommonURI().isERC681()
