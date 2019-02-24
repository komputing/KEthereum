package org.kethereum.model

data class ChainDefinition(
        val id: ChainId,
        private val prefix: String) {
    override fun toString() = "$prefix:${id.value}"
}
