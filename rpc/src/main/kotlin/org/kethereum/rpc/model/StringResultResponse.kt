package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class StringResultResponse(val result: String?) : BaseResponse()