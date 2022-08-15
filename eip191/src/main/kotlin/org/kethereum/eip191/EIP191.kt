package org.kethereum.eip191

import org.kethereum.crypto.signMessage
import org.kethereum.crypto.signedMessageToKey
import org.kethereum.crypto.toSignatureData
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PublicKey
import org.kethereum.model.SignatureData
import org.komputing.khex.model.HexString

/*
*
* http://eips.ethereum.org/EIPS/eip-191
*
*/

private const val MESSAGE_PREFIX: Byte = 0x19
private const val PERSONAL_SIGN_VERSION: Byte = 0x45

fun ECKeyPair.signWithEIP191(version: Byte, versionSpecificData: ByteArray, message: ByteArray) =
    signMessage(fullMessage(version, versionSpecificData, message))

fun ECKeyPair.signWithEIP191PersonalSign(message: ByteArray) =
    signWithEIP191(PERSONAL_SIGN_VERSION, personalSignVersionData(message), message)

fun personallySignedMessageToKey(message: ByteArray, signature: String): PublicKey =
    personallySignedMessageToKey(message, HexString(signature).toSignatureData())

fun personallySignedMessageToKey(message: ByteArray, signature: SignatureData): PublicKey =
    signedMessageToKey(
        fullMessage(PERSONAL_SIGN_VERSION, personalSignVersionData(message), message),
        signature
    )

private fun personalSignVersionData(message: ByteArray): ByteArray =
    ("thereum Signed Message:\n" + message.size).toByteArray()

private fun fullMessage(
    version: Byte,
    versionSpecificData: ByteArray,
    message: ByteArray,
): ByteArray =
    MESSAGE_PREFIX.toByteArray() + version.toByteArray() + versionSpecificData + message

private fun Byte.toByteArray() = ByteArray(1) { this }
