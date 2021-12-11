package org.kethereum.metadata.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Docs(
        val details: String?,
        val methods: Map<String, Any>
)