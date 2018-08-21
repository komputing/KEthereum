package org.kethereum.rpc.model

import org.kethereum.rpc.model.rpc.BlockInformationRPC

internal data class BlockInformationResponse(val jsonrpc: String, val id: String, val result: BlockInformationRPC)