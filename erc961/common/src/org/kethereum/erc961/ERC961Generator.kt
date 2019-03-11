package org.kethereum.erc961

import org.kethereum.model.Token

fun Token.generateURL(): String {

    val params = mutableListOf<String>()

    params.add("symbol=$symbol")

    if (decimals != 18) {
        params.add("decimals=$decimals")
    }

    name?.let {
        params.add("name=$it")
    }

    type?.let {
        params.add("type=$it")
    }

    return "ethereum:token_info-$address@${chain.id}?" + params.joinToString("&")
}