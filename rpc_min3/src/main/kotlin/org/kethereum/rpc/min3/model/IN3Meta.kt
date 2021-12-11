package org.kethereum.rpc.min3.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IN3Meta(
        val execTime: Int,
        val lastValidatorChange: Int
)