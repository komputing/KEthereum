package org.kethereum.hashes

import org.spongycastle.jcajce.provider.digest.RIPEMD160
import org.spongycastle.jcajce.provider.digest.SHA256

fun ByteArray.sha256() = SHA256.Digest().let {
    it.update(this)
    it.digest()
}!!

fun ByteArray.ripemd160() = RIPEMD160.Digest().let {
    it.update(this)
    it.digest()
}!!