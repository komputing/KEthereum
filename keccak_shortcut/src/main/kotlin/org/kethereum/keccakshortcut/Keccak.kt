package org.kethereum.keccakshortcut

import org.kethereum.crypto.api.hashing.keccackDigest256
import org.walleth.khex.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = keccackDigest256().let {
    it.update(this)
    it.digest()
}
