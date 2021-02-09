package org.walleth.kethereum.blockscout

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.model.ChainId

private const val BLOCKSCOUTCOM_BASE_URL = "https://blockscout.com"

private val BLOCKSCOUT_URLS = mapOf(
        BigInteger(1L) to "$BLOCKSCOUTCOM_BASE_URL/eth/mainnet",
        BigInteger(42L) to "$BLOCKSCOUTCOM_BASE_URL/eth/kovan",
        BigInteger(61L) to "$BLOCKSCOUTCOM_BASE_URL/etc/mainnet",
        BigInteger(77L) to "$BLOCKSCOUTCOM_BASE_URL/poa/sokol",
        BigInteger(99L) to "$BLOCKSCOUTCOM_BASE_URL/poa/core",
        BigInteger(100L) to "$BLOCKSCOUTCOM_BASE_URL/poa/xdai",

        BigInteger(43110L) to "http://athexplorer.ava.network"
)

val ALL_BLOCKSCOUT_SUPPORTED_NETWORKS = BLOCKSCOUT_URLS.map { it.key }.toSet()

fun getBlockscoutBaseURL(chain: ChainId) = BLOCKSCOUT_URLS[chain.value]

fun getBlockScoutBlockExplorer(chain: ChainId) = BLOCKSCOUT_URLS[chain.value]?.let { BlockScoutBlockExplorer(it) }