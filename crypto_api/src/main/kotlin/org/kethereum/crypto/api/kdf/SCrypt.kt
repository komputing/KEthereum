package org.kethereum.crypto.api.kdf

interface SCrypt {
    fun derive(password: ByteArray, salt: ByteArray?, n: Int, r: Int, p: Int, dklen: Int): ByteArray
}
