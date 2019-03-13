package org.kethereum.rpc.model

import kotlinx.serialization.Optional

open class BaseResponse(
        val jsonrpc: String = "",
        val id: String = "",
        @Optional val error: Error? = null
)