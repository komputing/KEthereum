package org.kethereum.model

data class ChainDefinition(
        val id: Long,
        private val prefix: String = "ETH") {
    override fun toString() = prefix + ":" + id
}
