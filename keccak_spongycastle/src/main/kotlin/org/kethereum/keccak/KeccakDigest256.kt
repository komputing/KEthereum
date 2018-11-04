package org.kethereum.keccak

import org.spongycastle.jcajce.provider.digest.Keccak

class KeccakDigest256 {
    private val internalDigest = Keccak.Digest256()
    fun update(data: ByteArray) =
        internalDigest.update(data)

    fun digest(): ByteArray =
        internalDigest.digest()
}
