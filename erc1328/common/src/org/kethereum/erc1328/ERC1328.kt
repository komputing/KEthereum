package org.kethereum.erc1328

const val ERC1328_PREFIX = "wc"

data class ERC1328(
        var sessionID: String? = null,

        var version: Long? = null,
        var name: String? = null,
        var bridge: String? = null,
        var symKey: String? = null,

        var valid: Boolean = true
)