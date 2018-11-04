package org.kethereum.cryptoapi.hashing


interface KeccakDigest256 {
    fun update(data: ByteArray)

    fun digest(): ByteArray
}
