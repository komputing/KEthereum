package org.kethereum.crypto.api.hashing


interface KeccakDigest256 {
    fun update(data: ByteArray)

    fun digest(): ByteArray
}
