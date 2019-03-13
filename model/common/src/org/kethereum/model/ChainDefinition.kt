package org.kethereum.model

import kotlinx.serialization.Serializable
import org.kethereum.model.serializer.ChainIdSerializer

// TODO: This should be an inline class as soon as kotlinx.serialization supports inline classes
@Serializable(with = ChainIdSerializer::class)
data class ChainId(val value: Long)

@Serializable
data class ChainDefinition(val id: ChainId, private val prefix: String) {
    override fun toString() = "$prefix:${id.value}"
}
