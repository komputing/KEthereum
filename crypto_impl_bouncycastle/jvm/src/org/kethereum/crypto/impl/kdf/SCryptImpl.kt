package org.kethereum.crypto.impl.kdf

class SCryptImpl: SCrypt {
    override fun derive(password: ByteArray, salt: ByteArray?, n: Int, r: Int, p: Int, dklen: Int) =
        org.bouncycastle.crypto.generators.SCrypt.generate(password, salt, n, r, p, dklen)
}
