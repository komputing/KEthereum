package org.kethereum.eip137

import org.kethereum.keccakshortcut.keccak
import org.komputing.khex.extensions.toHexString

inline class ENSName(val string: String)
inline class ENSNameHash(val byteArray: ByteArray)

/**
 * NameHash as defined here.
 * https://eips.ethereum.org/EIPS/eip-137
 */
fun ENSName.toNameHashByteArray(): ByteArray {
    var currentNameHash = ByteArray(32) { 0 }

    if (string.isNotEmpty()) {
        string.split(".").reversed().forEach {
            currentNameHash = (currentNameHash + it.toByteArray().keccak()).keccak()
        }
    }

    return currentNameHash
}

fun ENSName.toNameHash() = ENSNameHash(toNameHashByteArray())
fun ENSNameHash.toHexString() = byteArray.toHexString()

