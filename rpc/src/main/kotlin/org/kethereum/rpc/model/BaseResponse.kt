package org.kethereum.rpc.model

internal open class BaseResponse(
        val jsonrpc: String = "",
        val id: String = "",
        val error: Error? = null
)