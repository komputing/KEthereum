package org.kethereum.keccakshortcut

import org.spongycastle.jcajce.provider.digest.Keccak
import org.walleth.khex.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = Keccak.Digest256().let {
    it.update(this)
    it.digest()
}
