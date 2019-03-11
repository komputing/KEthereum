package org.kethereum.hashes

import org.kethereum.ripemd160.calculateRIPEMD160fromByteArray

fun ByteArray.sha256() = Sha256.digest(this)

fun ByteArray.ripemd160() = calculateRIPEMD160fromByteArray(this)
