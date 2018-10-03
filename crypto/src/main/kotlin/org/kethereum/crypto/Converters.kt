package org.kethereum.crypto

import org.kethereum.crypto.model.*
import org.kethereum.extensions.toHexStringZeroPadded
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.spongycastle.math.ec.ECPoint
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString
import java.math.BigInteger
import java.security.KeyPair
import java.util.*


fun PublicKey.toAddress() : Address {
    val publicKeyHexString = key.toHexStringZeroPadded(PUBLIC_KEY_LENGTH_IN_HEX, false)
    val hexToByteArray = publicKeyHexString.hexToByteArray()
    val hash = hexToByteArray.keccak().toHexString()

    return Address(hash.substring(hash.length - ADDRESS_LENGTH_IN_HEX))  // right most 160 bits
}

fun ECKeyPair.toAddress() = publicKey.toAddress()

fun KeyPair.toECKeyPair(): ECKeyPair {
    val privateKey = private as BCECPrivateKey
    val publicKey = public as BCECPublicKey

    val privateKeyValue = privateKey.d

    // Ethereum does not use encoded public keys like bitcoin - see
    // https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
    // Additionally, as the first bit is a constant prefix (0x04) we ignore this value
    val publicKeyBytes = publicKey.q.getEncoded(false)
    val publicKeyValue = BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.size))

    return ECKeyPair(PrivateKey(privateKeyValue), PublicKey(publicKeyValue))
}


fun ECKeyPair.toCredentials() = Credentials(this, toAddress())

fun PrivateKey.toECKeyPair() = ECKeyPair(this, publicKeyFromPrivate(this))


/**
 * Decodes an uncompressed public key (without 0x04 prefix) given an ECPoint
 */
fun ECPoint.toPublicKey() = getEncoded(false).let { encoded ->
    PublicKey(BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.size)))
}
