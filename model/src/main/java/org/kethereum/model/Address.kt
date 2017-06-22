package org.kethereum.model

data class Address(val hex: String) {

    override fun equals(other: Any?)
            = other is Address && other.hex.toUpperCase() == hex.toUpperCase()

    override fun hashCode() = hex.toUpperCase().hashCode()
}
