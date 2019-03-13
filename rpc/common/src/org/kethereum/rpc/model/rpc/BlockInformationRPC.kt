package org.kethereum.rpc.model.rpc

import kotlinx.serialization.Serializable

@Serializable
internal data class BlockInformationRPC(val transactions: List<TransactionRPC>)