package org.kethereum.crypto.api.ec

import java.math.BigInteger

typealias PrivateAndPublicKey = Pair<BigInteger, BigInteger>

interface KeyPairGenerator {
    fun generate(): PrivateAndPublicKey
}
