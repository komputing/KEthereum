package org.walleth.kethereum.blockscout

import org.kethereum.model.ChainId

private const val BLOCKSCOUTCOM_BASE_URL = "https://blockscout.com"

private val BLOCKSCOUT_URLS = mapOf(
        1L.toBigInteger() to "$BLOCKSCOUTCOM_BASE_URL/eth/mainnet",
        42L.toBigInteger() to "$BLOCKSCOUTCOM_BASE_URL/eth/kovan",
        61L.toBigInteger() to "$BLOCKSCOUTCOM_BASE_URL/etc/mainnet",
        77L.toBigInteger() to "$BLOCKSCOUTCOM_BASE_URL/poa/sokol",
        99L.toBigInteger() to "$BLOCKSCOUTCOM_BASE_URL/poa/core",
        100L.toBigInteger() to "$BLOCKSCOUTCOM_BASE_URL/poa/xdai",

        43110L.toBigInteger() to "http://athexplorer.ava.network"
)

val ALL_BLOCKSCOUT_SUPPORTED_NETWORKS = BLOCKSCOUT_URLS.map { it.key }.toSet()

fun getBlockscoutBaseURL(chain: ChainId) = BLOCKSCOUT_URLS[chain.value]

fun getBlockScoutBlockExplorer(chain: ChainId) = BLOCKSCOUT_URLS[chain.value]?.let { BlockScoutBlockExplorer(it) }