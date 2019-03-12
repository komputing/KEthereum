package org.kethereum.erc1328

const val ERC1328_SCHEMA = "wc"

data class ERC1328(
        var topic: String? = null,

        var version: Long? = null,
        var bridge: String? = null,
        var symKey: String? = null,

        var valid: Boolean = true
)