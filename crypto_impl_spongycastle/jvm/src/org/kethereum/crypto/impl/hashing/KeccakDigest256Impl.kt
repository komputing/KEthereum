package org.kethereum.crypto.impl.hashing

import org.spongycastle.jcajce.provider.digest.Keccak

class KeccakDigest256Impl: KeccakDigest256 {
    private val internalDigest = Keccak.Digest256()
    override fun update(data: ByteArray) =
        internalDigest.update(data)

    override fun digest(): ByteArray =
        internalDigest.digest()
}
