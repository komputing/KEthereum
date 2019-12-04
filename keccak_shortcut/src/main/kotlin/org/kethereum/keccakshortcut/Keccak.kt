package org.kethereum.keccakshortcut

import org.komputing.khash.keccak.Keccak
import org.komputing.khash.keccak.KeccakParameter
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

fun HexString.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = Keccak.digest(this, KeccakParameter.KECCAK_256)
