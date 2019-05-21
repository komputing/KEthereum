package org.walleth.kethereum.blockscout


private val BLOCKSCOUT_PATH = mapOf(
        1L to "eth/mainnet",
        3L to "eth/ropsten",
        4L to "eth/rinkeby",
        5L to "eth/goerli",
        42L to "eth/kovan",
        61L to "etc/mainnet",
        77L to "poa/sokol",
        99L to "poa/core",
        100L to "poa/dai"
)

val ALL_BLOCKSCOUT_SUPPORTED_NETWORKS = BLOCKSCOUT_PATH.map { it.key }.toSet()

private fun getPath(chain: Long) =  BLOCKSCOUT_PATH[chain]

fun getBlockscoutBaseURL(chain: Long) = "https://blockscout.com/" + getPath(chain)

fun getBlockScoutBlockExplorer(chain: Long) = BlockScoutBlockExplorer(getPath(chain))