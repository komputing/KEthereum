package org.kethereum.keccakshortcut

import org.kethereum.crypto.CryptoAPI
import org.walleth.khex.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = CryptoAPI.keccakDigest256.let {
    it.update(this)
    it.digest()
}
