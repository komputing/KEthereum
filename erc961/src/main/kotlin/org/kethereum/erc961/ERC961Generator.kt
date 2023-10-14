package org.kethereum.erc961

import org.kethereum.model.Token

private const val EIP_961_DEFAULT_DECIMALS = 18

/**
 * Create URL in [EIP-961](https://github.com/ethereum/EIPs/pull/961) format
 *
 * e.g. ethereum:token_info-0x00AB42@4?symbol=TST
 */
fun Token.generateURL(): String {

    val params = mutableListOf<String>()

    params.add("symbol=$symbol")

    if (decimals != EIP_961_DEFAULT_DECIMALS) {
        params.add("decimals=$decimals")
    }

    name?.let {
        params.add("name=$it")
    }

    type?.let {
        params.add("type=$it")
    }

    val joinedParams = params.joinToString("&")

    return "ethereum:token_info-$address@${chain.value}?$joinedParams"
}