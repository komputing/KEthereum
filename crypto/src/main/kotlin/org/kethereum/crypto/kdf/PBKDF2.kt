package org.kethereum.crypto.kdf

import org.spongycastle.crypto.Digest
import org.spongycastle.crypto.PBEParametersGenerator.PKCS5PasswordToUTF8Bytes
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.digests.SHA512Digest
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.params.KeyParameter

interface PBKDF2 {
    fun derive(pass: ByteArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

    fun derive(pass: CharArray, salt: ByteArray?, iterations: Int = 2048, digestParams: DigestParams = DigestParams.Sha512): ByteArray

    sealed class DigestParams(val keySize: Int) {
        object Sha256 : DigestParams(256)
        object Sha512 : DigestParams(512)
    }
}

class SpongyPBKDF2 : PBKDF2 {

    override fun derive(pass: ByteArray, salt: ByteArray?, iterations: Int, digestParams: PBKDF2.DigestParams): ByteArray {
        val gen = PKCS5S2ParametersGenerator(digestParams.toDigest())
        gen.init(pass, salt, iterations)
        return (gen.generateDerivedParameters(digestParams.keySize) as KeyParameter).key
    }

    override fun derive(pass: CharArray, salt: ByteArray?, iterations: Int, digestParams: PBKDF2.DigestParams): ByteArray =
        derive(PKCS5PasswordToUTF8Bytes(pass), salt, iterations, digestParams)

    fun PBKDF2.DigestParams.toDigest(): Digest =
            when(this) {
                PBKDF2.DigestParams.Sha256 -> SHA256Digest()
                PBKDF2.DigestParams.Sha512 -> SHA512Digest()
            }

}
