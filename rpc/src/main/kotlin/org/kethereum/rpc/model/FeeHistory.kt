package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeeHistory(val oldestBlock: String,
                      val baseFeePerGas: List<String>,
                      val gasUsedRatio: List<Float>,
                      val reward: List<List<String>>?)