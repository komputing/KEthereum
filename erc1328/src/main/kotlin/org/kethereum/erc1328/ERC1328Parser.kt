package org.kethereum.erc1328

import org.kethereum.model.EthereumURI
import org.kethereum.uri.common.parseCommonURI

fun EthereumURI.toERC1328() = ERC1328().apply {

    val commonURI = parseCommonURI(uri)

    valid = commonURI.valid && commonURI.prefix == ERC1328_SCHEMA && commonURI.scheme == "ethereum"

    version = commonURI.chainId?.value?.toLong() ?: 1L
    topic = commonURI.address
    val queryAsMap = commonURI.query.toMap()

    bridge = queryAsMap["bridge"]
    symKey = queryAsMap["symKey"]

}

fun parseERC1328(url: String) = EthereumURI(url).toERC1328()