@file:JvmName("BIP32")

package org.kethereum.bip32

import org.kethereum.bip44.BIP44

fun generateKey(seed: ByteArray, pathString: String) = BIP44(pathString)
        .path
        .fold(ExtendedKey.createFromSeed(seed)) { current, biP44Element ->
            current.generateChildKey(biP44Element)
        }

