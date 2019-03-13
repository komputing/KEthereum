package org.kethereum.wallet.model

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


const val AES_128_CTR = "pbkdf2"
const val SCRYPT = "scrypt"

@Serializable
data class CipherParams(var iv: String)

@Serializable
data class WalletCrypto(
    var cipher: String,
    var ciphertext: String,
    var cipherparams: CipherParams,
    var kdf: String,
    @Serializable(with = KdfParams.Companion::class) var kdfparams: KdfParams,
    var mac: String
)

@Serializable
internal data class WalletForImport(
    @Optional var address: String? = null,
    @Optional var crypto: WalletCrypto? = null,
    @Optional @SerialName("Crypto") var cryptoFromMEW: WalletCrypto? = null, //for MyEtherWallet json
    var id: String? = null,
    var version: Int = 0
) {
    init {
        // Make sure either crypto or crypto from MEW is not null
        check(crypto != null || cryptoFromMEW != null)
    }
}

@Serializable
data class Wallet(
    val address: String?,
    val crypto: WalletCrypto,
    val id: String,
    val version: Int
)
