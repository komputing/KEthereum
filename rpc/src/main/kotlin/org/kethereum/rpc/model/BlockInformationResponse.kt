package org.kethereum.rpc.model

import com.squareup.moshi.JsonClass
import org.kethereum.rpc.model.rpc.BlockInformationRPC

@JsonClass(generateAdapter = true)
internal data class BlockInformationResponse(val result: BlockInformationRPC) : BaseResponse()