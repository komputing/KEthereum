package org.kethereum.rpc.model

data class TransactionRPC(val value: String,
                       val from: String,
                       val to: String?,

                       val blockNumber: String,
                       val nonce: String,
                       val gasPrice: String,
                       val gas: String,
                       val hash: String,
                       val blockHash: String,
                       val input: String,
                       val transactionIndex: String,
                       val v: String,
                       val r: String,
                       val s: String)
