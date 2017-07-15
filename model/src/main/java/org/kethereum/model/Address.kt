package org.kethereum.model

data class Address(private val input: String) {

    val cleanHex = input.replace("0x","")

    @Transient
    val hex = "0x$cleanHex"

    override fun toString() = "0x" + cleanHex

    override fun equals(other: Any?)
            = other is Address && other.cleanHex.toUpperCase() == cleanHex.toUpperCase()

    override fun hashCode() = input.toUpperCase().hashCode()
}
