package org.kethereum.bip32

import org.kethereum.bip32.model.*
import org.kethereum.crypto.CURVE
import org.kethereum.crypto.CryptoAPI
import org.kethereum.crypto.decompressKey
import org.kethereum.crypto.toECKeyPair
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PRIVATE_KEY_SIZE
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import org.komputing.kbase58.decodeBase58WithChecksum
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.InvalidKeyException
import java.security.KeyException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

fun Seed.toExtendedKey(publicKeyOnly: Boolean = false, testnet: Boolean = false): ExtendedKey {
    try {
        val lr = CryptoAPI.hmac.init(BITCOIN_SEED).generate(seed)
        val l = lr.copyOfRange(0, PRIVATE_KEY_SIZE)
        val r = lr.copyOfRange(PRIVATE_KEY_SIZE, PRIVATE_KEY_SIZE + CHAINCODE_SIZE)
        val m = BigInteger(1, l)
        if (m >= CURVE.n) {
            throw KeyException("Master key creation resulted in a key with higher modulus. Suggest deriving the next increment.")
        }
        val keyPair = PrivateKey(l).toECKeyPair()
        return if (publicKeyOnly) {
            val pubKeyPair = ECKeyPair(PrivateKey(BigInteger.ZERO), keyPair.publicKey)
            ExtendedKey(pubKeyPair, r, 0, 0, 0, if (testnet) tpub else xpub)
        } else {
            ExtendedKey(keyPair, r, 0, 0, 0, if (testnet) tprv else xprv)
        }
    } catch (e: NoSuchAlgorithmException) {
        throw KeyException(e)
    } catch (e: NoSuchProviderException) {
        throw KeyException(e)
    } catch (e: InvalidKeyException) {
        throw KeyException(e)
    }

}


fun XPriv.toExtendedKey(): ExtendedKey {
    val data = xPriv.decodeBase58WithChecksum()
    if (data.size != EXTENDED_KEY_SIZE) {
        throw KeyException("invalid extended key")
    }

    val buff = ByteBuffer
            .wrap(data)
            .order(ByteOrder.BIG_ENDIAN)

    val versionBytes = ByteArray(4)

    buff.get(versionBytes)

    val hasPrivate = when {
        versionBytes.contentEquals(xprv) || versionBytes.contentEquals(tprv) -> true
        versionBytes.contentEquals(xpub) || versionBytes.contentEquals(tpub) -> false
        else -> throw KeyException("invalid version bytes for an extended key")
    }

    val depth = buff.get()
    val parent = buff.int
    val sequence = buff.int

    val chainCode = ByteArray(PRIVATE_KEY_SIZE)
    buff.get(chainCode)

    val keyPair = if (hasPrivate) {
        buff.get() // ignore the leading 0
        val privateBytes = ByteArray(PRIVATE_KEY_SIZE)
        buff.get(privateBytes)
        PrivateKey(privateBytes).toECKeyPair()
    } else {
        val compressedPublicBytes = ByteArray(COMPRESSED_PUBLIC_KEY_SIZE)
        buff.get(compressedPublicBytes)
        val uncompressedPublicBytes = decompressKey(compressedPublicBytes)
        ECKeyPair(
                PrivateKey(BigInteger.ZERO),
                PublicKey(BigInteger(1, uncompressedPublicBytes))
        )
    }
    return ExtendedKey(keyPair, chainCode, depth, parent, sequence, versionBytes)
}
