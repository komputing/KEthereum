package org.kethereum.rpc.model

internal data class Error(
        val message: String,
        val code: Int
)