package org.kethereum.erc681

import org.kethereum.model.ChainId
import java.math.BigInteger

data class ERC681(
        var valid: Boolean = true,
        var prefix: String? = null,
        var address: String? = null,
        var function: String? = null,
        var functionParams: List<Pair<String, String>> = listOf(),
        var scheme: String? = null,
        var chainId: ChainId? = null,

        var value: BigInteger? = null,
        var gas: BigInteger? = null
)