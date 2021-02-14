package org.kethereum.erc681

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.model.ChainId
import org.kethereum.model.EthereumFunctionCall

data class ERC681(
        var valid: Boolean = true,
        var prefix: String? = null,
        var address: String? = null,
        var scheme: String? = null,
        var chainId: ChainId? = null,
        var value: BigInteger? = null,
        var gasPrice: BigInteger? = null,
        var gasLimit: BigInteger? = null,

        override var function: String? = null,
        override var functionParams: List<Pair<String, String>> = listOf()
) : EthereumFunctionCall