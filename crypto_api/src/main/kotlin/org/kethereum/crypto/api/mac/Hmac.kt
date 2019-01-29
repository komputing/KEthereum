package org.kethereum.crypto.api.mac

import org.kethereum.crypto.impl.hashing.DigestParams

interface Hmac {
    fun init(key: ByteArray, digestParams: DigestParams = DigestParams.Sha512): Hmac

    fun generate(data: ByteArray): ByteArray
}
