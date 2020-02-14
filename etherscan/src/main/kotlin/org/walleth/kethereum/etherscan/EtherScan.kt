package org.walleth.kethereum.etherscan

import org.kethereum.model.ChainId
import java.math.BigInteger


private val ETHERSCAN_PREFIX_MAP = mapOf(
        BigInteger.valueOf(1L) to "",
        BigInteger.valueOf(3L) to "ropsten",
        BigInteger.valueOf(4L) to "rinkeby",
        BigInteger.valueOf(5L) to "goerli",
        BigInteger.valueOf(42L) to "kovan"
)

val ALL_ETHERSCAN_SUPPORTED_NETWORKS = ETHERSCAN_PREFIX_MAP.map { it.key }.toSet()

private fun getPrefix(chain: ChainId) = ETHERSCAN_PREFIX_MAP[chain.value]

private fun getAPIPrefix(chain: ChainId) = getPrefix(chain)?.let {
    if (it.isNotEmpty()) "-$it" else it
}

fun getEtherScanAPIBaseURL(chain: ChainId) = "https://api" + getAPIPrefix(chain) + ".etherscan.io"

fun getEtherScanBlockExplorer(chain: ChainId) = getPrefix(chain)?.let { EtherScanBlockExplorer(it) }