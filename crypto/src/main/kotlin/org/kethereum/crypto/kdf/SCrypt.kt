package org.kethereum.crypto.kdf

interface SCrypt {
    fun derive(password: ByteArray, salt: ByteArray?, n: Int, r: Int, p: Int, dklen: Int): ByteArray
}

class SpongySCrypt: SCrypt {
    override fun derive(password: ByteArray, salt: ByteArray?, n: Int, r: Int, p: Int, dklen: Int) =
        org.spongycastle.crypto.generators.SCrypt.generate(password, salt, n, r, p, dklen)
}
