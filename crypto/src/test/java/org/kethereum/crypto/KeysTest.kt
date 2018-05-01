package org.kethereum.crypto

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.kethereum.crypto.data.ADDRESS_NO_PREFIX
import org.kethereum.crypto.data.PUBLIC_KEY
import org.kethereum.crypto.data.PUBLIC_KEY_STRING
import org.kethereum.extensions.toBytesPadded
import org.walleth.khex.toHexString
import java.math.BigInteger

class KeysTest {

    @Before
    fun init() {
        initializeCrypto()
    }

    @Test
    fun testCreateSecp256k1KeyPair() {
        val keyPair = createSecp256k1KeyPair()
        val privateKey = keyPair.private
        val publicKey = keyPair.public

        assertNotNull(privateKey)
        assertNotNull(publicKey)

        assertThat(privateKey.encoded.size, `is`(144))
        assertThat(publicKey.encoded.size, `is`(88))
    }

    @Test
    fun testCreateEcKeyPair() {
        val (privateKey, publicKey) = createEcKeyPair()
        assertThat(publicKey.signum(), `is`(1))
        assertThat(privateKey.signum(), `is`(1))
    }

    @Test
    fun testGetAddressString() {
        assertThat(getAddress(PUBLIC_KEY_STRING),
                `is`(ADDRESS_NO_PREFIX))
    }

    @Test
    fun testGetAddressZeroPaddedAddress() {
        val publicKey = "0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22" + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113"
        assertThat(getAddress(publicKey),
                `is`("01c52b08330e05d731e38c856c1043288f7d9744"))
    }

    @Test
    fun testGetAddressBigInteger() {
        assertThat(getAddress(PUBLIC_KEY),
                `is`(ADDRESS_NO_PREFIX))
    }

    @Test
    fun testGetAddressSmallPublicKey() {
        val address = getAddress(BigInteger.valueOf(0x1234).toBytesPadded(PUBLIC_KEY_SIZE))
        val expected = address.toHexString("")

        assertThat(getAddress("0x1234"), equalTo(expected))
    }

    @Test
    fun testGetAddressZeroPadded() {
        val address = getAddress(BigInteger.valueOf(0x1234).toBytesPadded(PUBLIC_KEY_SIZE))
        val expected = address.toHexString("")

        val value = "1234"
        assertThat(getAddress("0x"
                + "0".repeat(PUBLIC_KEY_LENGTH_IN_HEX - value.length) + value),
                equalTo(expected))
    }

}
