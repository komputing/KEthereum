package org.walleth.kethereum.etherscan

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.model.ChainId


private val ETHERSCAN_PREFIX_MAP = mapOf(
        BigInteger(1L) to "",
        BigInteger(3L) to "ropsten",
        BigInteger(4L) to "rinkeby",
        BigInteger(5L) to "goerli",
        BigInteger(42L) to "kovan"
)

val ALL_ETHERSCAN_SUPPORTED_NETWORKS = ETHERSCAN_PREFIX_MAP.map { it.key }.toSet()

private fun getPrefix(chain: ChainId) = ETHERSCAN_PREFIX_MAP[chain.value]

private fun getAPIPrefix(chain: ChainId) = getPrefix(chain)?.let {
    if (it.isNotEmpty()) "-$it" else it
}

fun getEtherScanAPIBaseURL(chain: ChainId) = "https://api" + getAPIPrefix(chain) + ".etherscan.io"

fun getEtherScanBlockExplorer(chain: ChainId) = getPrefix(chain)?.let { EtherScanBlockExplorer(it) }