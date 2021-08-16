package org.kethereum.rpc.model

data class FeeHistory(val oldestBlock: String,
                      val baseFeePerGas: List<String>,
                      val gasUsedRatio: List<Float>,
                      val reward: List<List<String>>)