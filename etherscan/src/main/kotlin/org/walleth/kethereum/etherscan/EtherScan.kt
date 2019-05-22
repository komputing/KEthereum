package org.walleth.kethereum.etherscan

import org.kethereum.model.ChainDefinition
import java.math.BigInteger


private val ETHERSCAN_PREFIX_MAP = mapOf(
        BigInteger.valueOf(1L) to null,
        BigInteger.valueOf(3L) to "ropsten",
        BigInteger.valueOf(4L) to "rinkeby",
        BigInteger.valueOf(5L) to "goerli",
        BigInteger.valueOf(42L) to "kovan"
)

val ALL_ETHERSCAN_SUPPORTED_NETWORKS = ETHERSCAN_PREFIX_MAP.map { it.key }.toSet()

private fun getPrefix(chain: ChainDefinition) = ETHERSCAN_PREFIX_MAP[chain.id.value]

private fun getAPIPrefix(chain: ChainDefinition) = getPrefix(chain)?.let { "-$it" } ?: ""
fun getEtherScanAPIBaseURL(chain: ChainDefinition) = "https://api" + getAPIPrefix(chain) + ".etherscan.io"

fun getEtherScanBlockExplorer(chain: ChainDefinition) = EtherScanBlockExplorer(getPrefix(chain))