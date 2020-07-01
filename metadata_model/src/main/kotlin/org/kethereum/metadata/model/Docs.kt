package org.kethereum.metadata.model

data class Doc(
        val details: String
)

data class Docs(
        val details: String,
        val methods: Map<String, Any>
)