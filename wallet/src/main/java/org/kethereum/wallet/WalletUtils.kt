package org.kethereum.wallet

import kotlinx.serialization.json.JSON
import org.kethereum.crypto.ECKeyPair
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object WalletUtils {


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
        destination.writeText(JSON.stringify(walletFile))
        return fileName
    }

    @Throws(IOException::class, CipherException::class)
    fun loadKeysFromWalletFile(password: String, source: File) = JSON
            .parse<WalletFile>(source.readText())
            .decrypt(password)

    private fun getWalletFileName(walletFile: WalletFile): String {
        val dateFormat = SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(Date())
        return dateFormat.format(Date()) + walletFile.address + ".json"
    }
}
