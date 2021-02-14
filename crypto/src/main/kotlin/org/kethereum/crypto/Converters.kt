package org.kethereum.crypto

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import org.kethereum.crypto.api.ec.CurvePoint
import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString

fun PublicKey.toAddress() : Address {
    val publicKeyHexString = HexString(key.toHexStringZeroPadded(PUBLIC_KEY_LENGTH_IN_HEX, false))
    val hexToByteArray = publicKeyHexString.hexToByteArray()
    val hash = hexToByteArray.keccak().toHexString()

    return Address(hash.substring(hash.length - ADDRESS_LENGTH_IN_HEX))  // right most 160 bits
}

fun ECKeyPair.toAddress() = publicKey.toAddress()

fun ECKeyPair.toCredentials() = Credentials(this, toAddress())

fun PrivateKey.toECKeyPair() = ECKeyPair(this, publicKeyFromPrivate(this))


/**
 * Decodes an uncompressed public key (without 0x04 prefix) given an ECPoint
 */
fun CurvePoint.toPublicKey() = encoded().let { encoded ->
    PublicKey(BigInteger.fromByteArray(encoded.copyOfRange(1, encoded.size), Sign.POSITIVE))
}
