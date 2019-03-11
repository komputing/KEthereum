package org.kethereum.crypto.impl.kdf

class SCryptImpl: SCrypt {
    override fun derive(password: ByteArray, salt: ByteArray?, n: Int, r: Int, p: Int, dklen: Int): ByteArray =
        org.spongycastle.crypto.generators.SCrypt.generate(password, salt, n, r, p, dklen)
}
