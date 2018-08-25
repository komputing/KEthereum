package org.kethereum.wallet.model

import com.squareup.moshi.Json


const val AES_128_CTR = "pbkdf2"
const val SCRYPT = "scrypt"

data class CipherParams(var iv: String)

data class WalletCrypto(
        var cipher: String,
        var ciphertext: String,
        var cipherparams: CipherParams,
        var kdf: String,
        var kdfparams: KdfParams,
        var mac: String)


internal data class WalletForImport(

        var address: String? = null,

        var crypto: WalletCrypto? = null,

        @field:Json(name = "Crypto") //for MyEtherWallet json
        var cryptoFromMEW: WalletCrypto? = null,

        var id: String? = null,
        var version: Int = 0
)

data class Wallet(val address: String?,
                  val crypto: WalletCrypto,
                  val id: String,
                  val version: Int)
