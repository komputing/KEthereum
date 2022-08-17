package org.kethereum.eip191

import org.kethereum.crypto.signMessage
import org.kethereum.crypto.signedMessageToKey
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PublicKey
import org.kethereum.model.SignatureData
import java.security.SignatureException

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

/**
 * Given an arbitrary piece of text and an Ethereum message signature encoded in bytes,
 * returns the public key that was used to sign it. This can then be compared to the expected
 * public key to determine if the signature was correct.
 *
 * @param message RLP encoded message.
 * @param signature The message signature components
 * @return the public key used to sign the message
 * @throws SignatureException If the public key could not be recovered or if there was a
 * signature format error.
 */
@Throws(SignatureException::class)
fun personalSignedMessageToPublicKey(message: ByteArray, signature: SignatureData): PublicKey =
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
