package org.kethereum.wallet

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import org.junit.Before
import org.junit.Test
import org.kethereum.crypto.CryptoAPI
import org.kethereum.crypto.impl.BouncyCastleCryptoAPIProvider
import org.kethereum.model.extensions.toHexStringNoPrefix
import org.kethereum.wallet.data.*
import org.kethereum.wallet.model.WalletForImport
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 *
 */
class WalletJvmTest {

    @Before
    fun setup() {
        CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider)
    }

    @Test
    fun testCreateStandard() {
        assertEquals(KEY_PAIR.createWallet(PASSWORD, STANDARD_SCRYPT_CONFIG).address, ADDRESS_NO_PREFIX)
    }

    @Test
    fun testCreateLight() {
        assertEquals(KEY_PAIR.createWallet(PASSWORD, LIGHT_SCRYPT_CONFIG).address, ADDRESS_NO_PREFIX)
    }

    @Test
    fun testEncryptDecryptStandard() {
        assertEquals(KEY_PAIR.createWallet(PASSWORD, STANDARD_SCRYPT_CONFIG).decrypt(PASSWORD), KEY_PAIR)
    }

    @Test
    fun testEncryptDecryptLight() {
        assertEquals(KEY_PAIR.createWallet(PASSWORD, LIGHT_SCRYPT_CONFIG).decrypt(PASSWORD), KEY_PAIR)
    }

    @Test
    @ImplicitReflectionSerializer
    fun testDecryptAes128Ctr() {
        val walletFile = load(AES_128_CTR_TEST_JSON)
        val (privateKey) = walletFile.toTypedWallet().decrypt(PASSWORD)
        assertEquals(privateKey.key.toHexStringNoPrefix(), PRIVATE_KEY_STRING)
    }

    @Test
    @UseExperimental(ImplicitReflectionSerializer::class)
    fun testDecryptScrypt() {
        val walletFile = load(SCRYPT_TEST_JSON)
        val (privateKey) = walletFile.toTypedWallet().decrypt(PASSWORD)
        assertEquals(privateKey.key.toHexStringNoPrefix(), PRIVATE_KEY_STRING)
    }

    @Test
    fun testGenerateRandomBytes() {
        assertTrue(generateRandomBytes(0).contentEquals(byteArrayOf()))
        assertEquals(generateRandomBytes(10).size, 10)
    }

    @ImplicitReflectionSerializer
    private fun load(source: String): WalletForImport = Json.nonstrict.parse(source)
}