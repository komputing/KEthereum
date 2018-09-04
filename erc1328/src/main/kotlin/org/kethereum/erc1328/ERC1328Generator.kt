package org.kethereum.erc1328

fun ERC1328.generateURL(): String {
    var res = "ethereum:"

    res += "$ERC1328_PREFIX-"

    if (sessionID == null) {
        valid = false
    }

    res += sessionID

    if (version != null) {
        res += "@$version"
    }

    val paramList = mutableListOf<Pair<String, String>>()

    name?.let {
        paramList.add("name" to it)
    }

    bridge?.let {
        paramList.add("bridge" to it)
    }

    symKey?.let {
        paramList.add("symKey" to it)
    }

    if (paramList.isNotEmpty()) {
        res += "?" + paramList.joinToString("&") { it.first + "=" + it.second }
    }

    return res

}
