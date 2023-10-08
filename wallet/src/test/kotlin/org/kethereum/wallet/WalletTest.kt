package org.kethereum.wallet

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.wallet.data.*
import org.kethereum.wallet.model.WalletForImport

class WalletTest {

    @Test
    fun testCreateStandard() {
        val validWallet = KEY_PAIR.createWallet(PASSWORD, STANDARD_SCRYPT_CONFIG)
        assertThat(validWallet.address).isEqualTo(ADDRESS_NO_PREFIX)
    }

    @Test
    fun testCreateLight() {
        val validWallet = KEY_PAIR.createWallet(PASSWORD, LIGHT_SCRYPT_CONFIG)
        assertThat(validWallet.address).isEqualTo(ADDRESS_NO_PREFIX)
    }

    @Test
    fun testEncryptDecryptStandard() {
        val validWallet = KEY_PAIR.createWallet(PASSWORD, STANDARD_SCRYPT_CONFIG)
        val walletKeyPair = validWallet.decrypt(PASSWORD)
        assertThat(walletKeyPair).isEqualTo(KEY_PAIR)
    }

    @Test
    fun testEncryptDecryptLight() {
        val validWallet = KEY_PAIR.createWallet(PASSWORD, LIGHT_SCRYPT_CONFIG)
        val walletKeyPair = validWallet.decrypt(PASSWORD)
        assertThat(walletKeyPair).isEqualTo(KEY_PAIR)
    }

    @Test
    fun testDecryptAes128Ctr() {
        val walletFile = loadWalletFile(AES_128_CTR_TEST_JSON)
        val (keyPairFromFile) = walletFile.toTypedWallet().decrypt(PASSWORD)
        assertThat(keyPairFromFile.key.toHexStringNoPrefix()).isEqualTo(PRIVATE_KEY_STRING)
    }

    @Test
    fun testDecryptScrypt() {
        val walletFile = loadWalletFile(SCRYPT_TEST_JSON)
        val (keyPairFromFile) = walletFile.toTypedWallet().decrypt(PASSWORD)
        assertThat(keyPairFromFile.key.toHexStringNoPrefix()).isEqualTo(PRIVATE_KEY_STRING)
    }

    @Test
    fun testGenerateRandomBytes() {
        assertThat(generateRandomBytes(0)).isEqualTo(byteArrayOf())
        assertThat(generateRandomBytes(10).size).isEqualTo(10)
    }

    private fun loadWalletFile(source: String): WalletForImport =
        moshi.adapter(WalletForImport::class.java).fromJson(source)!!
}