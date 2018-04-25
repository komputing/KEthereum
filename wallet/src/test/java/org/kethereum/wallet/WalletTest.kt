package org.kethereum.wallet

import com.google.gson.GsonBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.wallet.data.*

class WalletTest {

    @Test
    fun testCreateStandard() {
        assertThat(KEY_PAIR.createStandard(PASSWORD).address).isEqualTo(ADDRESS_NO_PREFIX)
    }

    @Test
    fun testCreateLight() {
        assertThat(KEY_PAIR.createLight(PASSWORD).address).isEqualTo(ADDRESS_NO_PREFIX)
    }

    @Test
    fun testEncryptDecryptStandard() {
        assertThat(KEY_PAIR.createStandard(PASSWORD).decrypt(PASSWORD)).isEqualTo(KEY_PAIR)
    }

    @Test
    fun testEncryptDecryptLight() {
        assertThat(KEY_PAIR.createLight(PASSWORD).decrypt(PASSWORD)).isEqualTo(KEY_PAIR)
    }

    @Test
    fun testDecryptAes128Ctr() {
        val walletFile = load(AES_128_CTR)
        val (privateKey) = walletFile.decrypt(PASSWORD)
        assertThat(privateKey.toHexStringNoPrefix()).isEqualTo(PRIVATE_KEY_STRING)
    }

    @Test
    fun testDecryptScrypt() {
        val walletFile = load(SCRYPT)
        val (privateKey) = walletFile.decrypt(PASSWORD)
        assertThat(privateKey.toHexStringNoPrefix()).isEqualTo(PRIVATE_KEY_STRING)
    }

    @Test
    fun testGenerateRandomBytes() {
        assertThat(generateRandomBytes(0)).isEqualTo(byteArrayOf())
        assertThat(generateRandomBytes(10).size).isEqualTo(10)
    }

    private fun load(source: String) = GsonBuilder()
            .registerTypeAdapter(WalletFile.Crypto::class.java, WalletFile.CryptoTypeAdapter.INSTANCE)
            .create()
            .fromJson(source, WalletFile::class.java)
}