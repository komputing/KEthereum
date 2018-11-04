package org.kethereum.keccakshortcut

import org.kethereum.keccak.KeccakDigest256
import org.walleth.khex.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = KeccakDigest256().let {
    it.update(this)
    it.digest()
}
