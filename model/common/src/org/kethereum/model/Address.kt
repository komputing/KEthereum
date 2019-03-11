package org.kethereum.model

data class Address(private val input: String) {

    val cleanHex = input.removePrefix("0x")

    @Transient
    val hex = "0x$cleanHex"

    override fun toString() = hex

    override fun equals(other: Any?)
            = other is Address && other.cleanHex.toUpperCase() == cleanHex.toUpperCase()

    override fun hashCode() = cleanHex.toUpperCase().hashCode()
}
