@file:JvmName("BIP32")

package org.kethereum.bip32

import org.kethereum.bip44.BIP44


fun generateKey(seed: ByteArray, pathString: String): ExtendedKey {
    val master = ExtendedKey.createFromSeed(seed)

    var child = master
    BIP44(pathString).path.forEach {
        child = child.generateChildKey(it)
    }

    return child
}

