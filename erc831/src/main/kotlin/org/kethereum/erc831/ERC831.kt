package org.kethereum.erc831

/**
 * as defined in [EIP-831](https://eips.ethereum.org/EIPS/eip-831)
 */
data class ERC831(
    var scheme: String? = null,
    var prefix: String? = null,
    var payload: String? = null
)