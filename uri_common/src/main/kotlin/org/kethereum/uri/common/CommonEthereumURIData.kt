package org.kethereum.uri.common

import org.kethereum.model.ChainId

data class CommonEthereumURIData(
    var valid: Boolean = true,
    var scheme: String? = null,
    var prefix: String? = null,
    var chainId: ChainId? = null,
    var address: String? = null,
    var function: String? = null,
    var query: List<Pair<String, String>> = listOf()
)