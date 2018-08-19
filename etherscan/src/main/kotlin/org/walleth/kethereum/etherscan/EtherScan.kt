package org.walleth.kethereum.etherscan

import org.kethereum.model.ChainDefinition


private val ETHERSCAN_PREFIX_MAP = mapOf(
        1L to null,
        3L to "ropsten",
        4L to "rinkeby",
        42L to "kovan"
)

val ALL_ETHERSCAN_SUPPORTED_NETWORKS = ETHERSCAN_PREFIX_MAP.map { it.key }.toSet()

private fun getPrefix(chain: ChainDefinition) =  ETHERSCAN_PREFIX_MAP[chain.id]

private fun getAPIPrefix(chain: ChainDefinition) = getPrefix(chain)?.let { "-$it" } ?: ""
fun getEtherScanAPIBaseURL(chain: ChainDefinition) = "https://api" + getAPIPrefix(chain) + ".etherscan.io"

fun getEtherScanBlockExplorer(chain: ChainDefinition) = EtherScanBlockExplorer(getPrefix(chain))