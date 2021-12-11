package org.kethereum.rpc.min3.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IN3NodeListResult(val nodes: List<IN3Node>)

@JsonClass(generateAdapter = true)
data class IN3NodeListResponse(
        val id: Int,
        val in3: IN3Meta,
        val result: IN3NodeListResult,
        val jsonrpc: String
)