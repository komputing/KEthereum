package org.kethereum.wallet

import kotlinx.serialization.json.JSON
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.wallet.data.*
import org.kethereum.wallet.model.WalletForImport

class WalletTest {

    @Test
    fun testCreateStandard() {
        assertThat(KEY_PAIR.createWallet(PASSWORD, STANDARD_SCRYPT_CONFIG).address).isEqualTo(ADDRESS_NO_PREFIX)
    }

    @Test
    fun testCreateLight() {
        assertThat(KEY_PAIR.createWallet(PASSWORD, LIGHT_SCRYPT_CONFIG).address).isEqualTo(ADDRESS_NO_PREFIX)
    }

    @Test
    fun testEncryptDecryptStandard() {
        assertThat(KEY_PAIR.createWallet(PASSWORD, STANDARD_SCRYPT_CONFIG).decrypt(PASSWORD)).isEqualTo(KEY_PAIR)
    }

    @Test
    fun testEncryptDecryptLight() {
        assertThat(KEY_PAIR.createWallet(PASSWORD, LIGHT_SCRYPT_CONFIG).decrypt(PASSWORD)).isEqualTo(KEY_PAIR)
    }

    @Test
    fun testDecryptAes128Ctr() {
        val walletFile = load(AES_128_CTR_TEST_JSON)
        val (privateKey) = walletFile.toTypedWallet().decrypt(PASSWORD)
        assertThat(privateKey.toHexStringNoPrefix()).isEqualTo(PRIVATE_KEY_STRING)
    }

    @Test
    fun testDecryptScrypt() {
        val walletFile = load(SCRYPT_TEST_JSON)
        val (privateKey) = walletFile.toTypedWallet().decrypt(PASSWORD)
        assertThat(privateKey.toHexStringNoPrefix()).isEqualTo(PRIVATE_KEY_STRING)
    }

    @Test
    fun testGenerateRandomBytes() {
        assertThat(generateRandomBytes(0)).isEqualTo(byteArrayOf())
        assertThat(generateRandomBytes(10).size).isEqualTo(10)
    }

    private fun load(source: String): WalletForImport = JSON.parse(source)
}