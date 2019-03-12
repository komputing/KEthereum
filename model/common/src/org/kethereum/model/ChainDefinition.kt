package org.kethereum.model

inline class ChainId(val value: Long)

data class ChainDefinition(
    val id: ChainId,
    private val prefix: String
) {
    override fun toString() = "$prefix:${id.value}"
}
