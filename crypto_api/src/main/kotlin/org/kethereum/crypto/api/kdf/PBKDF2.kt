package org.kethereum.crypto.api.kdf

import org.kethereum.crypto.api.hashing.DigestParams

interface PBKDF2 {
    fun derive(pass: ByteArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

    fun derive(pass: CharArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

}
