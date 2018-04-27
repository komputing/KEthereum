package org.kethereum.wallet

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


const val AES_128_CTR = "pbkdf2"
const val SCRYPT = "scrypt"

@Serializable
data class CipherParams(var iv: String)

@Serializable
data class WalletFileCrypto(
        var cipher: String,
        var ciphertext: String,
        var cipherparams: CipherParams,
        var kdf: String,
        var kdfparams: Map<String, String>,
        var mac: String
)

@Serializable
data class WalletFile(

        @Optional
        var address: String? = null,

        @Optional
        var crypto: WalletFileCrypto? = null,

        @Optional
        @SerialName("Crypto") //for MyEtherWallet json
        var cryptoV2: WalletFileCrypto? = null,

        var id: String? = null,
        var version: Int = 0
)

fun WalletFile.getCrypto() = if (crypto == null) cryptoV2 else crypto

sealed class KdfParams {
    var dklen: Int = 0
    var salt: String? = null
}

data class Aes128CtrKdfParams(var c: Int = 0, var prf: String? = null) : KdfParams()

data class ScryptKdfParams(var n: Int = 0, var p: Int = 0, var r: Int = 0) : KdfParams()

fun WalletFileCrypto.toKdfParams() = if (kdf == SCRYPT) {
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
