package org.kethereum.crypto.impl.kdf

import org.kethereum.crypto.impl.hashing.DigestParams

interface PBKDF2 {
    fun derive(pass: ByteArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

    fun derive(pass: CharArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

}
