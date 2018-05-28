package org.kethereum.crypto

import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.SignatureData
import org.spongycastle.asn1.x9.X9IntegerConverter
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.ec.CustomNamedCurves
import org.spongycastle.crypto.params.ECDomainParameters
import org.spongycastle.crypto.params.ECPrivateKeyParameters
import org.spongycastle.crypto.signers.ECDSASigner
import org.spongycastle.crypto.signers.HMacDSAKCalculator
import org.spongycastle.math.ec.ECAlgorithms
import org.spongycastle.math.ec.ECPoint
import org.spongycastle.math.ec.FixedPointCombMultiplier
import org.spongycastle.math.ec.custom.sec.SecP256K1Curve
import java.math.BigInteger
import java.security.SignatureException
import java.util.*
import kotlin.experimental.and

/**
 *
 * Transaction signing logic.
 *
 *
 * Adapted from the
 * [BitcoinJ ECKey](https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/core/ECKey.java) implementation.
 */
private val CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1")
val CURVE = ECDomainParameters(
        CURVE_PARAMS.curve, CURVE_PARAMS.g, CURVE_PARAMS.n, CURVE_PARAMS.h)
private val HALF_CURVE_ORDER = CURVE_PARAMS.n.shiftRight(1)

@Deprecated("Please use extension function signMessage on ECKeyPair")
fun signMessage(message: ByteArray, keyPair: ECKeyPair) = keyPair.signMessage(message)

/**
 * Signs the [keccak] hash of the [message] buffer.
 * The signature is canonicalised ( @see [ECDSASignature.toCanonicalised] )
 *
 * @return [SignatureData] containing the (r,s,v) components
 */

fun ECKeyPair.signMessage(message: ByteArray) =
        signMessageHash(message.keccak(), this, true)


/**
 * Signs the the [messageHash] buffer with the private key in the given [keyPair].
 * If the [toCanonical] param is true, the signature is canonicalised ( @see [ECDSASignature.toCanonicalised] )
 *
 * This method is provided to allow for more flexible signature schemes using the same Ethereum keys.
 *
 * @return [SignatureData] containing the (r,s,v) components
 */
fun signMessageHash(messageHash: ByteArray, keyPair: ECKeyPair, toCanonical: Boolean = true): SignatureData {
    val privateKey = keyPair.privateKey
    val publicKey = keyPair.publicKey
    val sig = sign(messageHash, privateKey, toCanonical)
    // Now we have to work backwards to figure out the recId needed to recover the public key
    var recId = -1
    for (i in 0..3) {
        val k = recoverFromSignature(i, sig, messageHash)
        if (k != null && k == publicKey) {
            recId = i
            break
        }
    }
    if (recId == -1) {
        throw RuntimeException(
                "Could not construct a recoverable key. This should never happen.")
    }

    val headerByte = recId + 27

    val v = headerByte.toByte()

    return SignatureData(sig.r, sig.s, v)
}

private fun sign(transactionHash: ByteArray, privateKey: BigInteger, canonical: Boolean): ECDSASignature {
    val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest()))

    val ecPrivateKeyParameters = ECPrivateKeyParameters(privateKey, CURVE)
    signer.init(true, ecPrivateKeyParameters)
    val components = signer.generateSignature(transactionHash)

    val signature = ECDSASignature(components[0], components[1])
    return if (canonical) {
        signature.toCanonicalised()
    } else {
        signature
    }
}

/**
 *
 * Given the components of a signature and a selector value, recover and return the public
 * key that generated the signature according to the algorithm in SEC1v2 section 4.1.6.
 *
 *
 * The recId is an index from 0 to 3 which indicates which of the 4 possible keys is the
 * correct one. Because the key recovery operation yields multiple potential keys, the correct
 * key must either be stored alongside the
 * signature, or you must be willing to try each recId in turn until you find one that outputs
 * the key you are expecting.
 *
 *
 * If this method returns null it means recovery was not possible and recId should be
 * iterated.
 *
 *
 * Given the above two points, a correct usage of this method is inside a for loop from
 * 0 to 3, and if the output is null OR a key that is not the one you expect, you try again
 * with the next recId.
 *
 * @param recId Which possible key to recover.
 * @param sig the R and S components of the signature, wrapped.
 * @param message org.kethereum.crypto.Hash of the data that was signed.
 * @return An ECKey containing only the public part, or null if recovery wasn't possible.
 */
private fun recoverFromSignature(recId: Int, sig: ECDSASignature, message: ByteArray?): BigInteger? {
    require(recId >= 0) { "recId must be positive" }
    require(sig.r.signum() >= 0) { "r must be positive" }
    require(sig.s.signum() >= 0) { "s must be positive" }
    require(message != null) { "message cannot be null" }

    // 1.0 For j from 0 to h   (h == recId here and the loop is outside this function)
    //   1.1 Let x = r + jn
    val n = CURVE.n  // Curve order.
    val i = BigInteger.valueOf(recId.toLong() / 2)
    val x = sig.r.add(i.multiply(n))
    //   1.2. Convert the integer x to an octet string X of length mlen using the conversion
    //        routine specified in Section 2.3.7, where mlen = ⌈(log2 p)/8⌉ or mlen = ⌈m/8⌉.
    //   1.3. Convert the octet string (16 set binary digits)||X to an elliptic curve point R
    //        using the conversion routine specified in Section 2.3.4. If this conversion
    //        routine outputs “invalid”, then do another iteration of Step 1.
    //
    // More concisely, what these points mean is to use X as a compressed public key.
    val prime = SecP256K1Curve.q
    if (x >= prime) {
        // Cannot have point co-ordinates larger than this as everything takes place modulo Q.
        return null
    }
    // Compressed keys require you to know an extra bit of data about the y-coord as there are
    // two possibilities. So it's encoded in the recId.
    val r = decompressKey(x, recId and 1 == 1)
    //   1.4. If nR != point at infinity, then do another iteration of Step 1 (callers
    //        responsibility).
    if (!r.multiply(n).isInfinity) {
        return null
    }
    //   1.5. Compute e from M using Steps 2 and 3 of ECDSA signature verification.
    val e = BigInteger(1, message!!)
    //   1.6. For k from 1 to 2 do the following.   (loop is outside this function via
    //        iterating recId)
    //   1.6.1. Compute a candidate public key as:
    //               Q = mi(r) * (sR - eG)
    //
    // Where mi(x) is the modular multiplicative inverse. We transform this into the following:
    //               Q = (mi(r) * s ** R) + (mi(r) * -e ** G)
    // Where -e is the modular additive inverse of e, that is z such that z + e = 0 (mod n).
    // In the above equation ** is point multiplication and + is point addition (the EC group
    // operator).
    //
    // We can find the additive inverse by subtracting e from zero then taking the mod. For
    // example the additive inverse of 3 modulo 11 is 8 because 3 + 8 mod 11 = 0, and
    // -3 mod 11 = 8.
    val eInv = BigInteger.ZERO.subtract(e).mod(n)
    val rInv = sig.r.modInverse(n)
    val srInv = rInv.multiply(sig.s).mod(n)
    val eInvrInv = rInv.multiply(eInv).mod(n)
    val q = ECAlgorithms.sumOfTwoMultiplies(CURVE.g, eInvrInv, r, srInv)

    val qBytes = q.getEncoded(false)
    // We remove the prefix
    return BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.size))
}

/** Decompress a compressed public key (x co-ord and low-bit of y-coord).  */
private fun decompressKey(xBN: BigInteger, yBit: Boolean): ECPoint {
    val x9 = X9IntegerConverter()
    val compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.curve))
    compEnc[0] = (if (yBit) 0x03 else 0x02).toByte()
    return CURVE.curve.decodePoint(compEnc)
}

/**
 * Given an arbitrary piece of text and an Ethereum message signature encoded in bytes,
 * returns the public key that was used to sign it. This can then be compared to the expected
 * public key to determine if the signature was correct.
 *
 * @param message RLP encoded message.
 * @param signatureData The message signature components
 * @return the public key used to sign the message
 * @throws SignatureException If the public key could not be recovered or if there was a
 * signature format error.
 */
@Throws(SignatureException::class)
fun signedMessageToKey(message: ByteArray, signatureData: SignatureData): BigInteger {

    val header = signatureData.v and 0xFF.toByte()
    // The header byte: 0x1B = first key with even y, 0x1C = first key with odd y,
    //                  0x1D = second key with even y, 0x1E = second key with odd y
    if (header < 27 || header > 34) {
        throw SignatureException("Header byte out of range: $header")
    }

    val sig = ECDSASignature(signatureData.r, signatureData.s)

    val messageHash = message.keccak()
    val recId = header - 27
    return recoverFromSignature(recId, sig, messageHash) ?: throw SignatureException("Could not recover public key from signature")
}

/**
 * Returns public key from the given private key.
 *
 * @param privateKey the private key to derive the public key from
 * @return BigInteger encoded public key
 */
fun publicKeyFromPrivate(privateKey: BigInteger): BigInteger {
    val point = publicPointFromPrivate(privateKey)

    val encoded = point.getEncoded(false)
    return BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.size))  // remove prefix
}

/**
 * Returns public key point from the given private key.
 */
private fun publicPointFromPrivate(privateKey: BigInteger): ECPoint {
    /*
     * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group
     * order, but that could change in future versions.
     */
    val postProcessedPrivateKey = if (privateKey.bitLength() > CURVE.n.bitLength()) {
        privateKey.mod(CURVE.n)
    } else {
        privateKey
    }
    return FixedPointCombMultiplier().multiply(CURVE.g, postProcessedPrivateKey)
}

private data class ECDSASignature internal constructor(val r: BigInteger, val s: BigInteger) {

    /**
     * Returns true if the S component is "low", that means it is below
     * [HALF_CURVE_ORDER]. See
     * [BIP62](https://github.com/bitcoin/bips/blob/master/bip-0062.mediawiki#Low_S_values_in_signatures).
     */
    fun isCanonical() = s <= HALF_CURVE_ORDER

    /**
     * Will automatically adjust the S component to be less than or equal to half the curve
     * order, if necessary. This is required because for every signature (r,s) the signature
     * (r, -s (mod N)) is a valid signature of the same message. However, we dislike the
     * ability to modify the bits of a Bitcoin transaction after it's been signed, as that
     * violates various assumed invariants. Thus in future only one of those forms will be
     * considered legal and the other will be banned.
     */
    fun toCanonicalised(): ECDSASignature {
        return if (!isCanonical()) {
            // The order of the curve is the number of valid points that exist on that curve.
            // If S is in the upper half of the number of valid points, then bring it back to
            // the lower half. Otherwise, imagine that
            //    N = 10
            //    s = 8, so (-8 % 10 == 2) thus both (r, 8) and (r, 2) are valid solutions.
            //    10 - 8 == 2, giving us always the latter solution, which is canonical.
            ECDSASignature(r, CURVE.n.subtract(s))
        } else {
            this
        }
    }
}
