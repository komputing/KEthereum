package org.kethereum.wallet

import kotlinx.serialization.json.JSON
import org.kethereum.crypto.ECKeyPair
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Throws(CipherException::class, IOException::class)
fun ECKeyPair.generateWalletFile(password: String,
                                 destinationDirectory: File,
                                 useFullScrypt: Boolean = false) = (if (useFullScrypt) createStandard(password) else createLight(password)).let {
    val fileName = it.getWalletFileName()
    File(destinationDirectory, fileName).apply {
        writeText(JSON.stringify(it))
    }
}

@Throws(IOException::class, CipherException::class)
fun File.loadKeysFromWalletFile(password: String) = JSON.parse<WalletFile>(readText()).decrypt(password)

private fun WalletFile.getWalletFileName() =
        SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date()) + address + ".json"