package org.kethereum.rpc.model

import kotlinx.serialization.Serializable
import org.kethereum.rpc.model.response.BaseResponse
import org.kethereum.rpc.model.rpc.BlockInformationRPC

@Serializable
internal data class BlockInformationResponse(val result: BlockInformationRPC) : BaseResponse()