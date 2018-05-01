package org.kethereum.wallet.model

import kotlinx.serialization.KOutput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

sealed class KdfParams {
    abstract var dklen: Int
    abstract var salt: String?
}

@Serializable
data class Aes128CtrKdfParams(var c: Int = 0,
                              var prf: String? = null,
                              override var dklen: Int = 0,
                              override var salt: String? = null) : KdfParams()

@Serializable
data class ScryptKdfParams(var n: Int = 0,
                           var p: Int = 0,
                           var r: Int = 0,

                           override var dklen: Int = 0,
                           override var salt: String? = null) : KdfParams()

@Serializer(forClass = KdfParams::class)
object KdfSerializer : KSerializer<KdfParams> {
    override fun save(output: KOutput, obj: KdfParams) = when (obj) {
        is ScryptKdfParams -> output.write(obj)
        is Aes128CtrKdfParams -> output.write(obj)
    }
}

fun WalletCryptoForImport.getTypedKdfParams() = if (kdf == SCRYPT) {
    ScryptKdfParams().apply {
        n = kdfparams["n"]!!.toInt()
        p = kdfparams["p"]!!.toInt()
        r = kdfparams["r"]!!.toInt()
    }
} else {
    Aes128CtrKdfParams().apply {
        c = kdfparams["c"]!!.toInt()
        prf = kdfparams["prf"]!!
    }
}.apply {
    salt = kdfparams["salt"]
    dklen = kdfparams["dklen"]!!.toInt()
}


