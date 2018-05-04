package org.kethereum.rpc.model

data class BlockInformationResponse(val jsonrpc: String, val id: String, val result: BlockInformation)