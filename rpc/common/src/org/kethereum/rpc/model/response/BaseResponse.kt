package org.kethereum.rpc.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(
    @Optional open val jsonrpc: String = "",
    @Optional open val id: String = "",
    @Optional open val error: Error? = null
)