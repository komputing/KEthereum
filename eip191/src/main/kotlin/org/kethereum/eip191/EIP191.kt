package org.kethereum.eip191

import org.kethereum.model.ECKeyPair
import org.kethereum.crypto.signMessage

/*
*
* http://eips.ethereum.org/EIPS/eip-191
*
*/


fun ECKeyPair.signWithEIP191(version: Byte, versionSpecificData: ByteArray, message: ByteArray) =
        signMessage(0x19.toByte().toByteArray() + version.toByteArray() + versionSpecificData + message)

fun ECKeyPair.signWithEIP191PersonalSign(message: ByteArray) =
        signWithEIP191(0x45, ("thereum Signed Message:\n" + message.size).toByteArray(), message)

private fun Byte.toByteArray() = ByteArray(1) { this }