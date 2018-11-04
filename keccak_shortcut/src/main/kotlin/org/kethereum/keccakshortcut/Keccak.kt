package org.kethereum.keccakshortcut

import org.kethereum.cryptoapi.hashing.keccackDigest256
import org.walleth.khex.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = keccackDigest256().let {
    it.update(this)
    it.digest()
}
