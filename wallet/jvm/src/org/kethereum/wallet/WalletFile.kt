package org.kethereum.wallet

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.kethereum.model.ECKeyPair
import org.kethereum.wallet.model.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Throws(CipherException::class, IOException::class)
@UseExperimental(ImplicitReflectionSerializer::class)
fun ECKeyPair.generateWalletFile(password: String, destinationDirectory: File, config: ScryptConfig) =
    createWallet(password, config).let { wallet ->
        FiledWallet(wallet, File(destinationDirectory, wallet.getWalletFileName()).apply {
            writeText(Json.stringify(wallet))
        })
    }

@Throws(CipherException::class, IOException::class)
@UseExperimental(ImplicitReflectionSerializer::class)
fun ECKeyPair.generateWalletJSON(password: String, config: ScryptConfig) =
    createWallet(password, config).let { wallet ->
        Json.stringify(wallet)
    }

@Throws(IOException::class, CipherException::class)
@UseExperimental(ImplicitReflectionSerializer::class)
fun File.loadKeysFromWalletFile(password: String) = readText().loadKeysFromWalletJsonString(password)

@Throws(CipherException::class)
@UseExperimental(ImplicitReflectionSerializer::class)
fun String.loadKeysFromWalletJsonString(password: String) =
    Json.parse<WalletForImport>(this).toTypedWallet().decrypt(password)

fun Wallet.getWalletFileName() =
    SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date()) + address + ".json"
