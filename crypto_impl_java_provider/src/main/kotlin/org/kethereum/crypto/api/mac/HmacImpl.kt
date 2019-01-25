package org.kethereum.crypto.api.mac

import org.kethereum.crypto.api.hashing.DigestParams
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HmacImpl : Hmac {

    private lateinit var mac: Mac

    override fun init(key: ByteArray, digestParams: DigestParams): Hmac {
        val version = digestParams.toHmacVersion()
        mac = Mac.getInstance(version)
        val keySpec = SecretKeySpec(key, version)
        mac.init(keySpec)
        return this
    }

    override fun generate(data: ByteArray): ByteArray =
        mac.doFinal(data)

    private fun DigestParams.toHmacVersion() = when(this) {
        DigestParams.Sha512 -> "HmacSHA512"
        DigestParams.Sha256 -> "HmacSHA256"
    }
}
