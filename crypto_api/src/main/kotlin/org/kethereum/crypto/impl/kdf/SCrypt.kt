package org.kethereum.crypto.impl.kdf

interface SCrypt {
    fun derive(password: ByteArray, salt: ByteArray?, n: Int, r: Int, p: Int, dklen: Int): ByteArray
}
