package org.kethereum.erc1328

fun ERC1328.generateURL(): String {
    var res = "$ERC1328_SCHEMA:"

    if (topic == null) {
        valid = false
    }

    res += topic

    if (version != null) {
        res += "@$version"
    }

    val paramList = mutableListOf<Pair<String, String>>()

    bridge?.let {
        paramList.add("bridge" to it)
    }

    symKey?.let {
        paramList.add("key" to it)
    }

    if (paramList.isNotEmpty()) {
        res += "?" + paramList.joinToString("&") { it.first + "=" + it.second }
    }

    return res

}
