package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass
import org.kethereum.rpc.model.rpc.TransactionRPC

@JsonClass(generateAdapter = true)
internal data class TransactionResponse(val result: TransactionRPC) : BaseResponse()