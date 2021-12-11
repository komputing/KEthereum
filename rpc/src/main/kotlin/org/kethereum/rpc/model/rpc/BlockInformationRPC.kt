package org.kethereum.rpc.model.rpc

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class BlockInformationRPC(
    val number: String,
    val hash: String,
    val parentHash: String,
    val nonce: String,
    val sha3Uncles: String,
    val logsBloom: String,
    val transactionsRoot: String,
    val stateRoot: String,
    val miner: String,
    val difficulty: String,
    val totalDifficulty: String,
    val extraData: String,
    val size: String,
    val gasLimit: String,
    val gasUsed: String,
    val timestamp: String,
    val uncles: List<String>,
    val transactions: List<TransactionRPC>,
    val baseFeePerGas: String?
)