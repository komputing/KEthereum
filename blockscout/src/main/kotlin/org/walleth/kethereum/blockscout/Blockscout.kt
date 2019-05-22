package org.walleth.kethereum.blockscout

import org.kethereum.model.ChainId


private val BLOCKSCOUT_PATH = mapOf(
        1L.toBigInteger() to "eth/mainnet",
        3L.toBigInteger() to "eth/ropsten",
        4L.toBigInteger() to "eth/rinkeby",
        5L.toBigInteger() to "eth/goerli",
        42L.toBigInteger() to "eth/kovan",
        61L.toBigInteger() to "etc/mainnet",
        77L.toBigInteger() to "poa/sokol",
        99L.toBigInteger() to "poa/core",
        100L.toBigInteger() to "poa/dai"
)

val ALL_BLOCKSCOUT_SUPPORTED_NETWORKS = BLOCKSCOUT_PATH.map { it.key }.toSet()

private fun getPath(chain: ChainId) =  BLOCKSCOUT_PATH[chain.value]

fun getBlockscoutBaseURL(chain: ChainId) = "https://blockscout.com/" + getPath(chain)

fun getBlockScoutBlockExplorer(chain: ChainId) = BlockScoutBlockExplorer(getPath(chain))