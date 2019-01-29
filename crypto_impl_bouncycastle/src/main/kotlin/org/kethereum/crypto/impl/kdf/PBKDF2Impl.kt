package org.kethereum.crypto.impl.kdf

import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.PBEParametersGenerator.PKCS5PasswordToUTF8Bytes
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.bouncycastle.crypto.params.KeyParameter
import org.kethereum.crypto.impl.hashing.DigestParams

class PBKDF2Impl : PBKDF2 {

    override fun derive(pass: ByteArray, salt: ByteArray?, iterations: Int, digestParams: DigestParams): ByteArray {
        val gen = PKCS5S2ParametersGenerator(digestParams.toDigest())
        gen.init(pass, salt, iterations)
        return (gen.generateDerivedParameters(digestParams.keySize) as KeyParameter).key
    }

    override fun derive(pass: CharArray, salt: ByteArray?, iterations: Int, digestParams: DigestParams): ByteArray =
        derive(PKCS5PasswordToUTF8Bytes(pass), salt, iterations, digestParams)

    fun DigestParams.toDigest(): Digest =
            when(this) {
                DigestParams.Sha256 -> SHA256Digest()
                DigestParams.Sha512 -> SHA512Digest()
            }

}
