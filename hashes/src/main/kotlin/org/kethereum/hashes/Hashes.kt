package org.kethereum.hashes

import org.spongycastle.jcajce.provider.digest.RIPEMD160

fun ByteArray.sha256() = Sha256.Digest().digest(this)

fun ByteArray.ripemd160() = RIPEMD160.Digest().let {
    it.update(this)
    it.digest()
}!!