package org.kethereum.wallet

import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

const val AES_128_CTR = "pbkdf2"
const val SCRYPT = "scrypt"

data class CipherParams(var iv: String? = null)

data class WalletFileCrypto(
        var cipher: String? = null,
        var ciphertext: String? = null,
        var cipherparams: CipherParams? = null,
        var kdf: String? = null,
        var kdfparams: KdfParams? = null,
        var mac: String? = null
)

data class WalletFile(

        var address: String? = null,
        var crypto: WalletFileCrypto? = null,
        @SerializedName("Crypto") //for MyEtherWallet json
        var cryptoV2: WalletFileCrypto? = null,

        var id: String? = null,
        var version: Int = 0
)

fun WalletFile.getCrypto() = if (crypto == null) cryptoV2 else crypto

class CryptoTypeAdapter private constructor() : TypeAdapter<WalletFileCrypto>() {
    @Throws(IOException::class)
    override fun write(jout: JsonWriter, src: WalletFileCrypto) {
        jout.beginObject()
        jout
                .name("cipher").value(src.cipher)
                .name("ciphertext").value(src.ciphertext)
                .name("cipherparams")
                .beginObject().name("iv").value(src.cipherparams!!.iv).endObject()
                .name("kdf").value(src.kdf)
                .name("kdfparams").beginObject()
                .name("dklen").value(src.kdfparams!!.dklen)
        val kdfparams = src.kdfparams!!
        when (kdfparams) {
            is Aes128CtrKdfParams -> jout
                    .name("c").value(kdfparams.c)
                    .name("prf").value(kdfparams.prf)
            is ScryptKdfParams -> jout
                    .name("n").value(kdfparams.n)
                    .name("p").value(kdfparams.p)
                    .name("r").value(kdfparams.r)
        }
        jout
                .name("salt").value(kdfparams.salt).endObject()
                .name("mac").value(src.mac)
                .endObject()
    }

    @Throws(IOException::class)
    override fun read(jin: JsonReader): WalletFileCrypto {
        val kdfParamsMap = mutableMapOf<String, String>()
        val crypto = WalletFileCrypto()
        jin.beginObject()
        while (jin.hasNext()) {
            when (jin.nextName()) {
                "cipher" -> crypto.cipher = jin.nextString()
                "ciphertext" -> crypto.ciphertext = jin.nextString()

                "cipherparams" -> {
                    val cipherParams = CipherParams()
                    jin.beginObject()
                    while (jin.hasNext()) {
                        when (jin.nextName()) {
                            "iv" -> cipherParams.iv = jin.nextString()
                            else -> jin.skipValue()
                        }
                    }
                    jin.endObject()
                    crypto.cipherparams = cipherParams
                }
                "kdf" ->                     crypto.kdf = jin.nextString()

                "mac" ->                     crypto.mac = jin.nextString()
                "kdfparams" -> {
                    jin.beginObject()
                    while (jin.hasNext()) {
                        val name = jin.nextName()
                        val value = jin.nextString()
                        kdfParamsMap[name] = value
                    }
                    jin.endObject()
                }
                else -> jin.skipValue()
            }
        }
        jin.endObject()
        val kdfParams: KdfParams
        when (crypto.kdf) {
            AES_128_CTR -> {
                kdfParams = Aes128CtrKdfParams()
                kdfParams.c = kdfParamsMap["c"]!!.toInt()
                kdfParams.prf = kdfParamsMap["prf"]
            }
            SCRYPT -> {
                kdfParams = ScryptKdfParams()
                kdfParams.n = kdfParamsMap["n"]!!.toInt()
                kdfParams.p = kdfParamsMap["p"]!!.toInt()
                kdfParams.r = kdfParamsMap["r"]!!.toInt()
            }
            else -> throw IOException("KDF type is not supported")
        }
        kdfParams.dklen = kdfParamsMap["dklen"]!!.toInt()
        kdfParams.salt = kdfParamsMap["salt"]
        crypto.kdfparams = kdfParams
        return crypto
    }

    companion object {
        val INSTANCE = CryptoTypeAdapter().nullSafe()
    }
}


sealed class KdfParams {
    var dklen: Int = 0
    var salt: String? = null
}

data class Aes128CtrKdfParams(var c: Int = 0, var prf: String? = null) : KdfParams()
data class ScryptKdfParams(var n: Int = 0, var p: Int = 0, var r: Int = 0) : KdfParams()