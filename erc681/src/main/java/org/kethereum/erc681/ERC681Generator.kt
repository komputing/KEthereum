package org.kethereum.erc681

fun ERC681.generateURL(): String {
    var res = "ethereum:"
    if (prefix != null) {
        res += "$prefix-"
    }

    if (address != null) {
        res += address
    }

    if (chainId != 1L) {
        res += "@$chainId"
    }

    if (function != null) {
        res += "/$function"
    }

    val paramList = mutableMapOf<String, String>()
    if (gas != null) {
        paramList.put("gas", gas.toString())
    }
    if (value != null) {
        paramList.put("value", value.toString())
    }

    if (paramList.isNotEmpty()) {
        res += "?" + paramList.map { it.key + "=" + it.value }.joinToString("&")
    }

    return res

}
