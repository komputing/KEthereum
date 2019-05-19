package org.kethereum.keccakshortcut

import org.komputing.khash.keccak.Keccak
import org.komputing.khash.keccak.KeccakParameter
import org.walleth.khex.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = Keccak.digest(this, KeccakParameter.KECCAK_256)
