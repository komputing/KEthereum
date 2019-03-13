package org.kethereum.rpc.model.response

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class StringResultResponse(
    @Optional val result: String? = ""
) : BaseResponse()