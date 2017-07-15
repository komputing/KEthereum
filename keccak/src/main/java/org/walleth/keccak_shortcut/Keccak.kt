package org.walleth.keccak_shortcut

import org.walleth.keccak.Keccak
import org.walleth.keccak.Parameter.KECCAK_256

fun String.keccak() = Keccak().getHash(this, KECCAK_256)
