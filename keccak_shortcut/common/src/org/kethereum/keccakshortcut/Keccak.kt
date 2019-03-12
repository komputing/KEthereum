package org.kethereum.keccakshortcut

import org.kethereum.model.extensions.hexToByteArray

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = calculateSHA3(SHA3Parameter.KECCAK_256)
