package org.kethereum.model.secure

/**
 *
 */
expect class SecureRandom() {
    fun nextBytes(bytes: ByteArray)
}