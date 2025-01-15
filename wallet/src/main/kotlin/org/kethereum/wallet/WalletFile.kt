package org.kethereum.wallet

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.kethereum.model.ECKeyPair
import org.kethereum.wallet.model.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

internal class KDFJsonAdapter {
    @FromJson
    fun fromJson(map: Map<String, String>) = (when {
        map["prf"] != null -> Aes128CtrKdfParams(
                c = map["c"]!!.toInt(),
                prf = map["prf"]

        )
        map["n"] != null -> ScryptKdfParams(
                n = map["n"]!!.toInt(),
                p = map["p"]!!.toInt(),
                r = map["r"]!!.toInt(),
                salt = map["salt"],
                dklen = map["dklen"]!!.toInt()
        )
        else -> throw IllegalArgumentException("Could not detect KDFParams")
    }).apply {
        salt = map["salt"]
        dklen = map["dklen"]!!.toInt()
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: KdfParams) = when (value) {
        is ScryptKdfParams -> moshi.adapter(ScryptKdfParams::class.java).toJson(writer, value)
        is Aes128CtrKdfParams -> moshi.adapter(Aes128CtrKdfParams::class.java).toJson(writer, value)
    }

}

internal val moshi by lazy {
    Moshi.Builder()
            .add(KDFJsonAdapter())
            .build()
}

@Throws(CipherException::class, IOException::class)
fun ECKeyPair.generateWalletFile(
    password: String,
    destinationDirectory: File,
    config: ScryptConfig
): FiledWallet = createWallet(password, config).let { wallet ->
    FiledWallet(wallet, File(destinationDirectory, wallet.getWalletFileName()).apply {
        writeText(moshi.adapter(Wallet::class.java).toJson(wallet))
    })
}

@Throws(CipherException::class, IOException::class)
fun ECKeyPair.generateWalletJSON(password: String,
                                                     config: ScryptConfig) = createWallet(password, config).let { wallet ->
    moshi.adapter(Wallet::class.java).toJson(wallet)
}

@Throws(IOException::class, CipherException::class)
fun File.loadKeysFromWalletFile(password: String) = readText().loadKeysFromWalletJsonString(password)

@Throws(CipherException::class)
fun String.loadKeysFromWalletJsonString(password: String) = moshi.adapter(WalletForImport::class.java).fromJson(this)?.toTypedWallet()?.decrypt(password)

fun Wallet.getWalletFileName() =
        SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date()) + address + ".json"
