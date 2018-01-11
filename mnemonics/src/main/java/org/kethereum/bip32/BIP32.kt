package org.kethereum.bip32

import org.kethereum.bip44.BIP44

object BIP32 {

    fun generateKey(seed: ByteArray, path: String): ExtendedKey {
        val master = ExtendedKey.createFromSeed(seed)

        var child = master
        BIP44.fromPath(path).toIntList().forEach {
            child = child.generateChildKey(it)
        }

        return child
    }

}
