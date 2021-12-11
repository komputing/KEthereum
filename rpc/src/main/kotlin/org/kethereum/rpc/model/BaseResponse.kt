package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal open class BaseResponse(
        var jsonrpc: String = "",
        var id: String = "",
        var error: Error? = null
)