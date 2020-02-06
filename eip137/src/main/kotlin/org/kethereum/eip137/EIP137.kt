package org.kethereum.eip137

import org.kethereum.eip137.model.ENSName
import org.kethereum.eip137.model.ENSNameHash
import org.kethereum.keccakshortcut.keccak
import org.komputing.khex.extensions.toHexString

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

