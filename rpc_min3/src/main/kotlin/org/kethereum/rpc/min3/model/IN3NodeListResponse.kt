package org.kethereum.rpc.min3.model

data class IN3NodeListResult(val nodes: List<IN3Node>)
data class IN3NodeListResponse(
        val id: Int,
        val in3: IN3Meta,
        val result: IN3NodeListResult,
        val jsonrpc: String
)