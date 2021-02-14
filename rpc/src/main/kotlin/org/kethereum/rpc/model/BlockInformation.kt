package org.kethereum.rpc.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.model.SignedTransaction
import org.komputing.khex.model.HexString

data class BlockInformation(
        val number: BigInteger,
        val hash: HexString,
        val parentHash: HexString,
        val nonce: BigInteger,
        val sha3Uncles: HexString,
        val logsBloom: HexString,
        val transactionsRoot: HexString,
        val stateRoot: HexString,
        val miner: HexString,
        val difficulty: BigInteger,
        val totalDifficulty: BigInteger,
        val extraData: HexString,
        val size: BigInteger,
        val gasLimit: BigInteger,
        val gasUsed: BigInteger,
        val timestamp: BigInteger,
        val uncles: List<HexString>,
        val transactions: List<SignedTransaction>
)