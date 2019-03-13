package org.kethereum.rpc.model

import kotlinx.serialization.Serializable
import org.kethereum.model.SignedTransaction

@Serializable
data class BlockInformation(
    val transactions: List<SignedTransaction>
)