package org.kethereum.rpc.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val message: String,
    val code: Int
)