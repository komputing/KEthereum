package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class FeeHistoryResponse(val result: FeeHistory?) : BaseResponse()