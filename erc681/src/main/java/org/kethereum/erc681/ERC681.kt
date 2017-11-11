package org.kethereum.erc681

import java.math.BigInteger

data class ERC681(
        var valid: Boolean = true,
        var prefix: String? = null,
        var addressString: String? = null,
        var function: String? = null,
        var query: String = "",
        var scheme: String? = null,
        var chainId: Long = 1,

        var value: BigInteger? = null,
        var gas: BigInteger? = null
)