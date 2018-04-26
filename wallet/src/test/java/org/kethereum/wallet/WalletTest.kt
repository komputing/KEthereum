package org.kethereum.wallet

import com.google.gson.GsonBuilder
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.wallet.data.*
import java.io.IOException


class WalletTest {

    @Test
    @Throws(Exception::class)
    fun testCreateStandard() {
        testCreate(Wallet.createStandard(PASSWORD, KEY_PAIR))
    }

    @Test
    @Throws(Exception::class)
    fun testCreateLight() {
        testCreate(Wallet.createLight(PASSWORD, KEY_PAIR))
    }

    @Throws(Exception::class)
    private fun testCreate(walletFile: WalletFile) {
        assertThat(walletFile.address, `is`(ADDRESS_NO_PREFIX))
    }

    @Test
    @Throws(Exception::class)
    fun testEncryptDecryptStandard() {
        testEncryptDecrypt(Wallet.createStandard(PASSWORD, KEY_PAIR))
    }

    @Test
    @Throws(Exception::class)
    fun testEncryptDecryptLight() {
        testEncryptDecrypt(Wallet.createLight(PASSWORD, KEY_PAIR))
    }

    @Throws(Exception::class)
    private fun testEncryptDecrypt(walletFile: WalletFile) {
        assertThat(Wallet.decrypt(PASSWORD, walletFile), equalTo(KEY_PAIR))
    }

    @Test
    @Throws(Exception::class)
    fun testDecryptAes128Ctr() {
        val walletFile = load(AES_128_CTR)
        val (privateKey) = Wallet.decrypt(PASSWORD, walletFile)
        assertThat(privateKey.toHexStringNoPrefix(), `is`(PRIVATE_KEY_STRING))
    }

    @Test
    @Throws(Exception::class)
    fun testDecryptScrypt() {
        val walletFile = load(SCRYPT)
        val (privateKey) = Wallet.decrypt(PASSWORD, walletFile)
        assertThat(privateKey.toHexStringNoPrefix(), `is`(PRIVATE_KEY_STRING))
    }

    @Test
    fun testGenerateRandomBytes() {
        assertThat(Wallet.generateRandomBytes(0), `is`(byteArrayOf()))
        assertThat(Wallet.generateRandomBytes(10).size, `is`(10))
    }

    @Throws(IOException::class)
    private fun load(source: String): WalletFile {
        val gson = GsonBuilder()
                .registerTypeAdapter(WalletFile.Crypto::class.java, WalletFile.CryptoTypeAdapter.INSTANCE)
                .create()
        return gson.fromJson(source, WalletFile::class.java)
    }
}