package org.kethereum.model

data class Address(private val input: String) {

    val cleanHex = input.removePrefix("0x")

    @Transient
    val hex = "0x$cleanHex"

    override fun toString() = hex

    override fun equals(other: Any?)
            = other is Address && other.cleanHex.equals(cleanHex, ignoreCase = true)

    override fun hashCode() = cleanHex.uppercase().hashCode()
}
