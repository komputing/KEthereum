package org.kethereum.crypto.impl.ec

import org.bouncycastle.asn1.x9.X9IntegerConverter
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.math.ec.ECAlgorithms
import org.bouncycastle.math.ec.ECPoint
import org.bouncycastle.math.ec.FixedPointCombMultiplier
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve
import org.kethereum.crypto.api.ec.ECDSASignature
import org.kethereum.crypto.api.ec.Signer
import java.math.BigInteger
import java.util.*

class EllipticCurveSigner : Signer {

    override fun sign(transactionHash: ByteArray, privateKey: BigInteger, canonical: Boolean): ECDSASignature {
        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest()))

        val ecPrivateKeyParameters = ECPrivateKeyParameters(privateKey, DOMAIN_PARAMS)
        signer.init(true, ecPrivateKeyParameters)
        val components = signer.generateSignature(transactionHash)

        return ECDSASignature(components[0], components[1]).let {
            if (canonical) {
                it.canonicalise()
            } else {
                it
            }
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
    override fun recover(recId: Int, sig: ECDSASignature, message: ByteArray?): BigInteger? {
        require(recId >= 0) { "recId must be positive" }
        require(sig.r.signum() >= 0) { "r must be positive" }
        require(sig.s.signum() >= 0) { "s must be positive" }
        require(message != null) { "message cannot be null" }

        // 1.0 For j from 0 to h   (h == recId here and the loop is outside this function)
        //   1.1 Let x = r + jn
        val n = CURVE_PARAMS.n  // Curve order.
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
        val e = BigInteger(1, message)
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
        val q = ECAlgorithms.sumOfTwoMultiplies(CURVE_PARAMS.g, eInvrInv, r, srInv)

        val qBytes = q.getEncoded(false)
        // We remove the prefix
        return BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.size))
    }

    /** Decompress a compressed public key (x co-ord and low-bit of y-coord).  */
    private fun decompressKey(xBN: BigInteger, yBit: Boolean): ECPoint {
        val x9 = X9IntegerConverter()
        val compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE_PARAMS.curve))
        compEnc[0] = (if (yBit) 0x03 else 0x02).toByte()
        return DOMAIN_PARAMS.curve.decodePoint(compEnc)
    }

    override fun publicFromPrivate(privateKey: BigInteger): BigInteger {

        val point = publicPointFromPrivate(privateKey)

        val encoded = point.getEncoded(false)
        return BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.size))
    }

    /**
     * Returns public key point from the given private key.
     */
    private fun publicPointFromPrivate(privateKey: BigInteger): ECPoint {
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group
         * order, but that could change in future versions.
         */
        val postProcessedPrivateKey = if (privateKey.bitLength() > CURVE_PARAMS.n.bitLength()) {
            privateKey.mod(DOMAIN_PARAMS.n)
        } else {
            privateKey
        }
        return FixedPointCombMultiplier().multiply(DOMAIN_PARAMS.g, postProcessedPrivateKey)
    }

}
