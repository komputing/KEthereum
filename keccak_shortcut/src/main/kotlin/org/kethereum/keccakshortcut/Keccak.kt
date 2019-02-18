package org.kethereum.keccakshortcut

import org.walleth.khex.hexToByteArray
import org.walleth.sha3.SHA3Parameter
import org.walleth.sha3.calculateSHA3

fun String.keccak() = hexToByteArray().keccak()
fun ByteArray.keccak() = calculateSHA3(SHA3Parameter.KECCAK_256)
