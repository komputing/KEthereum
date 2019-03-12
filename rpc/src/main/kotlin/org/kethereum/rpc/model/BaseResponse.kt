package org.kethereum.rpc.model

open class BaseResponse(
        val jsonrpc: String = "",
        val id: String = "",
        val error: Error? = null
)