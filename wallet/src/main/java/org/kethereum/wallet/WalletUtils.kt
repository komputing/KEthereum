package org.kethereum.wallet

import com.google.gson.GsonBuilder
import org.kethereum.crypto.ECKeyPair
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object WalletUtils {

    private val gson = GsonBuilder()
            .registerTypeAdapter(WalletFile.Crypto::class.java, WalletFile.CryptoTypeAdapter.INSTANCE)
            .create()

    @Throws(CipherException::class, IOException::class)
    fun generateWalletFile(
            password: String,
            ecKeyPair: ECKeyPair,
            destinationDirectory: File,
            useFullScrypt: Boolean = false
    ): String {
        val walletFile = if (useFullScrypt) {
            ecKeyPair.createStandard(password)
        } else {
            ecKeyPair.createLight(password)
        }

        val fileName = getWalletFileName(walletFile)
        val destination = File(destinationDirectory, fileName)

        FileWriter(destination).use({ writer ->
            gson.toJson(walletFile, writer)
            return fileName
        })
    }

    @Throws(IOException::class, CipherException::class)
    fun loadKeysFromWalletFile(password: String, source: File): ECKeyPair {
        FileReader(source).use({ reader ->
            val walletFile = gson.fromJson<WalletFile>(reader, WalletFile::class.java)
            return walletFile.decrypt(password)
        })
    }

    private fun getWalletFileName(walletFile: WalletFile): String {
        val dateFormat = SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(Date())
        return dateFormat.format(Date()) + walletFile.address + ".json"
    }
}
