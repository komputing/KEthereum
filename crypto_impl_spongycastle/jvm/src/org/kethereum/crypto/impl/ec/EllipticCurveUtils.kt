package org.kethereum.crypto.impl.ec

import org.kethereum.crypto.api.ec.ECDSASignature

private val HALF_CURVE_ORDER = CURVE_PARAMS.n.shiftRight(1)
/**
 * Returns true if the S component is "low", that means it is below
 * [HALF_CURVE_ORDER]. See
 * [BIP62](https://github.com/bitcoin/bips/blob/master/bip-0062.mediawiki#Low_S_values_in_signatures).
 */
private fun ECDSASignature.isCanonical() = s <= HALF_CURVE_ORDER

/**
 * Will automatically adjust the S component to be less than or equal to half the curve
 * order, if necessary. This is required because for every signature (r,s) the signature
 * (r, -s (mod N)) is a valid signature of the same message. However, we dislike the
 * ability to modify the bits of a Bitcoin transaction after it's been signed, as that
 * violates various assumed invariants. Thus in future only one of those forms will be
 * considered legal and the other will be banned.
 */
fun ECDSASignature.canonicalise() = if (isCanonical()) {
    this
} else {
    // The order of the curve is the number of valid points that exist on that curve.
    // If S is in the upper half of the number of valid points, then bring it back to
    // the lower half. Otherwise, imagine that
    //    N = 10
    //    s = 8, so (-8 % 10 == 2) thus both (r, 8) and (r, 2) are valid solutions.
    //    10 - 8 == 2, giving us always the latter solution, which is canonical.
    ECDSASignature(r, CURVE_PARAMS.n.subtract(s))
}
