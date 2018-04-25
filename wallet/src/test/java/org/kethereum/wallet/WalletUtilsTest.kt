package org.kethereum.wallet

import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.kethereum.crypto.ECKeyPair
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.wallet.data.KEY_PAIR
import org.kethereum.wallet.data.PASSWORD
import java.io.File
import java.nio.file.Files


class WalletUtilsTest {

    private lateinit var tempDir: File

    @Before
    @Throws(Exception::class)
    fun setUp() {
        tempDir = createTempDir()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        for (file in tempDir.listFiles()) {
            file.delete()
        }
        tempDir.delete()
    }

    @Test
    @Throws(Exception::class)
    fun testGenerateFullWalletFile() {
        val fileName = WalletUtils.generateWalletFile(PASSWORD, KEY_PAIR, tempDir, true)
        testGenerateWalletFile(fileName)
    }

    @Test
    @Throws(Exception::class)
    fun testGenerateLightWalletFile() {
        val fileName = WalletUtils.generateWalletFile(PASSWORD, KEY_PAIR, tempDir, false)
        testGenerateWalletFile(fileName)
    }

    @Throws(Exception::class)
    private fun testGenerateWalletFile(fileName: String) {
        val keyPair = WalletUtils.loadKeysFromWalletFile(
                PASSWORD, File(tempDir, fileName))

        assertThat(keyPair, equalTo(KEY_PAIR))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadCredentialsFromFile() {
        val keyPair = WalletUtils.loadKeysFromWalletFile(
                PASSWORD,
                File(WalletUtilsTest::class.java.getResource(
                        "/keyfiles/"
                                + "UTC--2016-11-03T05-55-06."
                                + "340672473Z--ef678007d18427e6022059dbc264f27507cd1ffc")
                        .file))

        assertThat(keyPair, equalTo(KEY_PAIR))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadCredentialsMyEtherWallet() {
        val file = File(WalletUtilsTest::class.java.getResource(
                ("/keyfiles/"
                        + "UTC--2016-11-03T07-47-45."
                        + "988Z--4f9c1a1efaa7d81ba1cabf07f2c3a5ac5cf4f818")).file)
        val keyPair = WalletUtils.loadKeysFromWalletFile(
                PASSWORD, file)

        assertThat(keyPair, equalTo(ECKeyPair.create("6ca4203d715e693279d6cd9742ad2fb7a3f6f4abe27a64da92e0a70ae5d859c9".hexToBigInteger())))
    }

    @Throws(Exception::class)
    private fun createTempDir(): File {
        return Files.createTempDirectory(
                WalletUtilsTest::class.java.simpleName + "-testkeys").toFile()
    }
}