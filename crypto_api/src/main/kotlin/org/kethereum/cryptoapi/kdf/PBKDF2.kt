package org.kethereum.cryptoapi.kdf

interface PBKDF2 {
    fun derive(pass: ByteArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

    fun derive(pass: CharArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

    sealed class DigestParams(val keySize: Int) {
        object Sha256 : DigestParams(256)
        object Sha512 : DigestParams(512)
    }
}
