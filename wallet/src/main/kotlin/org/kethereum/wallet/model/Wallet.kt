package org.kethereum.wallet.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


const val AES_128_CTR = "pbkdf2"
const val SCRYPT = "scrypt"

@JsonClass(generateAdapter = true)
data class CipherParams(var iv: String)

@JsonClass(generateAdapter = true)
data class WalletCrypto(
        var cipher: String,
        var ciphertext: String,
        var cipherparams: CipherParams,
        var kdf: String,
        var kdfparams: KdfParams,
        var mac: String
)


@JsonClass(generateAdapter = true)
internal data class WalletForImport(

        var address: String? = null,

        var crypto: WalletCrypto? = null,

        @field:Json(name = "Crypto") //for MyEtherWallet json
        var cryptoFromMEW: WalletCrypto? = null,

        var id: String? = null,
        var version: Int = 0
)

@JsonClass(generateAdapter = true)
data class Wallet(val address: String?,
                  val crypto: WalletCrypto,
                  val id: String,
                  val version: Int)
