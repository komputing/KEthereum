package org.kethereum.crypto.impl.hashing


interface KeccakDigest256 {
    fun update(data: ByteArray)

    fun digest(): ByteArray
}
