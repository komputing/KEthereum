package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StringResultResponse(val result: String?) : BaseResponse()