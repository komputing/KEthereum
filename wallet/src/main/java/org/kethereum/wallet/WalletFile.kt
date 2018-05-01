package org.kethereum.wallet

import kotlinx.serialization.json.JSON
import org.kethereum.crypto.ECKeyPair
import org.kethereum.wallet.model.CipherException
import org.kethereum.wallet.model.Wallet
import org.kethereum.wallet.model.WalletForImport
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Throws(CipherException::class, IOException::class)
fun ECKeyPair.generateWalletFile(password: String,
                                 destinationDirectory: File,
                                 config: ScryptConfig) = createWalletFile(password, config).apply {
    File(destinationDirectory, getWalletFileName()).writeText(JSON.stringify(this))
}

@Throws(IOException::class, CipherException::class)
fun File.loadKeysFromWalletFile(password: String) = readText().loadKeysFromWalletJsonString(password)

@Throws( CipherException::class)
fun String.loadKeysFromWalletJsonString(password: String) = JSON.parse<WalletForImport>(this).toTypedWallet().decrypt(password)

fun Wallet.getWalletFileName() =
        SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date()) + address + ".json"