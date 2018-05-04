package org.kethereum.uri.common

data class CommonEthereumURIData(
        var valid: Boolean = true,
        var scheme: String? = null,
        var prefix: String? = null,
        var chainId: Long? = null,
        var address: String? = null,
        var function: String? = null,
        var query: List<Pair<String, String>> = listOf()
)