package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class StringArrayResponse(val result: List<String>) : BaseResponse()