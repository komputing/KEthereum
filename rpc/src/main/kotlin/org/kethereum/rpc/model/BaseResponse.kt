package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BaseResponse(
        var jsonrpc: String = "",
        var id: String = "",
        var error: Error? = null
)