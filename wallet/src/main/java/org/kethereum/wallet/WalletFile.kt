package org.kethereum.wallet

import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException


class WalletFile {

    companion object {
        const val AES_128_CTR = "pbkdf2"
        const val SCRYPT = "scrypt"
    }

    var address: String? = null
    var crypto: Crypto? = null
        get() = if (field == null) cryptoV2 else field

    @SerializedName("Crypto") //for MyEtherWallet json
    private var cryptoV2: Crypto? = null

    var id: String? = null
    var version: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (!(other is WalletFile)) {
            return false
        }

        val that = other as WalletFile?

        if (if (address != null)
                    address != that!!.address
                else
                    that!!.address != null) {
            return false
        }
        if (if (crypto != null)
                    crypto != that.crypto
                else
                    that.crypto != null) {
            return false
        }
        if (if (id != null)
                    id != that.id
                else
                    that.id != null) {
            return false
        }
        return version == that.version
    }

    override fun hashCode(): Int {
        var result = if (address != null) address!!.hashCode() else 0
        result = 31 * result + (if (crypto != null) crypto!!.hashCode() else 0)
        result = 31 * result + (if (id != null) id!!.hashCode() else 0)
        result = 31 * result + version
        return result
    }

    class Crypto {
        var cipher: String? = null
        var ciphertext: String? = null
        var cipherparams: CipherParams? = null

        var kdf: String? = null

        var kdfparams: KdfParams? = null

        var mac: String? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (!(other is Crypto)) {
                return false
            }

            val that = other as Crypto?

            if (if (cipher != null)
                        cipher != that!!.cipher
                    else
                        that!!.cipher != null) {
                return false
            }
            if (if (ciphertext != null)
                        ciphertext != that.ciphertext
                    else
                        that.ciphertext != null) {
                return false
            }
            if (if (cipherparams != null)
                        cipherparams != that.cipherparams
                    else
                        that.cipherparams != null) {
                return false
            }
            if (if (kdf != null)
                        kdf != that.kdf
                    else
                        that.kdf != null) {
                return false
            }
            if (if (kdfparams != null)
                        kdfparams != that.kdfparams
                    else
                        that.kdfparams != null) {
                return false
            }
            return if (mac != null)
                mac == that.mac
            else
                that.mac == null
        }

        override fun hashCode(): Int {
            var result = if (cipher != null) cipher!!.hashCode() else 0
            result = 31 * result + (if (ciphertext != null) ciphertext!!.hashCode() else 0)
            result = 31 * result + (if (cipherparams != null) cipherparams!!.hashCode() else 0)
            result = 31 * result + (if (kdf != null) kdf!!.hashCode() else 0)
            result = 31 * result + (if (kdfparams != null) kdfparams!!.hashCode() else 0)
            result = 31 * result + (if (mac != null) mac!!.hashCode() else 0)
            return result
        }

    }

    class CipherParams {
        var iv: String? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (!(other is CipherParams)) {
                return false
            }

            val that = other as CipherParams?

            return if (iv != null)
                iv == that!!.iv
            else
                that!!.iv == null
        }

        override fun hashCode(): Int {
            val result = if (iv != null) iv!!.hashCode() else 0
            return result
        }

    }

    interface KdfParams {
        var dklen: Int

        var salt: String?
    }

    class Aes128CtrKdfParams : KdfParams {
        override var dklen: Int = 0
        var c: Int = 0
        var prf: String? = null
        override var salt: String? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (!(other is Aes128CtrKdfParams)) {
                return false
            }

            val that = other as Aes128CtrKdfParams?

            if (dklen != that!!.dklen) {
                return false
            }
            if (c != that.c) {
                return false
            }
            if (if (prf != null)
                        prf != that.prf
                    else
                        that.prf != null) {
                return false
            }
            return if (salt != null)
                salt == that.salt
            else
                that.salt == null
        }

        override fun hashCode(): Int {
            var result = dklen
            result = 31 * result + c
            result = 31 * result + (if (prf != null) prf!!.hashCode() else 0)
            result = 31 * result + (if (salt != null) salt!!.hashCode() else 0)
            return result
        }
    }

    class ScryptKdfParams : KdfParams {
        override var dklen: Int = 0
        var n: Int = 0
        var p: Int = 0
        var r: Int = 0
        override var salt: String? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (!(other is ScryptKdfParams)) {
                return false
            }

            val that = other as ScryptKdfParams?

            if (dklen != that!!.dklen) {
                return false
            }
            if (n != that.n) {
                return false
            }
            if (p != that.p) {
                return false
            }
            if (r != that.r) {
                return false
            }
            return if (salt != null)
                salt == that.salt
            else
                that.salt == null
        }

        override fun hashCode(): Int {
            var result = dklen
            result = 31 * result + n
            result = 31 * result + p
            result = 31 * result + r
            result = 31 * result + (if (salt != null) salt!!.hashCode() else 0)
            return result
        }
    }

    class CryptoTypeAdapter private constructor() : TypeAdapter<Crypto>() {
        @Throws(IOException::class)
        override fun write(jout: JsonWriter, src: Crypto) {
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
                else -> throw IOException("KDF type is not supported")
            }
            jout
                    .name("salt").value(kdfparams.salt).endObject()
                    .name("mac").value(src.mac)
                    .endObject()
        }

        @Throws(IOException::class)
        override fun read(jin: JsonReader): Crypto {
            val kdfParamsMap = mutableMapOf<String, String>()
            val crypto = Crypto()
            jin.beginObject()
            while (jin.hasNext()) {
                when (jin.nextName()) {
                    "cipher" -> {
                        crypto.cipher = jin.nextString()
                    }
                    "ciphertext" -> {
                        crypto.ciphertext = jin.nextString()
                    }
                    "cipherparams" -> {
                        val cipherParams = CipherParams()
                        jin.beginObject()
                        while (jin.hasNext()) {
                            when (jin.nextName()) {
                                "iv" -> {
                                    cipherParams.iv = jin.nextString()
                                }
                                else -> jin.skipValue()
                            }
                        }
                        jin.endObject()
                        crypto.cipherparams = cipherParams
                    }
                    "kdf" -> {
                        crypto.kdf = jin.nextString()
                    }
                    "mac" -> {
                        crypto.mac = jin.nextString()
                    }
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

}
