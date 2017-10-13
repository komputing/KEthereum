package org.kethereum.crypto

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Test
import org.kethereum.crypto.Keys.PUBLIC_KEY_LENGTH_IN_HEX
import org.kethereum.crypto.Keys.PUBLIC_KEY_SIZE
import org.kethereum.crypto.data.*
import org.kethereum.extensions.toBytesPadded
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString
import java.math.BigInteger
import java.util.*

class KeysTest {

    @Test
    @Throws(Exception::class)
    fun testCreateSecp256k1KeyPair() {
        val keyPair = Keys.createSecp256k1KeyPair()
        val privateKey = keyPair.private
        val publicKey = keyPair.public

        assertNotNull(privateKey)
        assertNotNull(publicKey)

        assertThat(privateKey.encoded.size, `is`(144))
        assertThat(publicKey.encoded.size, `is`(88))
    }

    @Test
    @Throws(Exception::class)
    fun testCreateEcKeyPair() {
        val (privateKey, publicKey) = Keys.createEcKeyPair()
        assertThat(publicKey.signum(), `is`(1))
        assertThat(privateKey.signum(), `is`(1))
    }

    @Test
    fun testGetAddressString() {
        assertThat(Keys.getAddress(PUBLIC_KEY_STRING),
                `is`(ADDRESS_NO_PREFIX))
    }

    @Test
    fun testGetAddressZeroPaddedAddress() {
        val publicKey = "0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22" + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113"
        assertThat(Keys.getAddress(publicKey),
                `is`("01c52b08330e05d731e38c856c1043288f7d9744"))
    }

    @Test
    fun testGetAddressBigInteger() {
        assertThat(Keys.getAddress(PUBLIC_KEY),
                `is`(ADDRESS_NO_PREFIX))
    }

    @Test
    fun testGetAddressSmallPublicKey() {
        val address = Keys.getAddress(BigInteger.valueOf(0x1234).toBytesPadded(PUBLIC_KEY_SIZE))
        val expected = address.toHexString("")

        assertThat(Keys.getAddress("0x1234"), equalTo(expected))
    }

    @Test
    fun testGetAddressZeroPadded() {
        val address = Keys.getAddress(BigInteger.valueOf(0x1234).toBytesPadded(PUBLIC_KEY_SIZE))
        val expected = address.toHexString("")

        val value = "1234"
        assertThat(Keys.getAddress("0x"
                + "0".repeat(PUBLIC_KEY_LENGTH_IN_HEX - value.length) + value),
                equalTo(expected))
    }

    @Test
    fun testSerializeECKey() {
        assertThat(Keys.serialize(KEY_PAIR), `is`(ENCODED))
    }

    @Test
    fun testDeserializeECKey() {
        assertThat(Keys.deserialize(ENCODED), `is`(KEY_PAIR))
    }

    @Test(expected = RuntimeException::class)
    fun testDeserializeInvalidKey() {
        Keys.deserialize(ByteArray(0))
    }

    companion object {

        private val ENCODED: ByteArray

        init {
            val privateKey = PRIVATE_KEY_STRING.hexToByteArray()
            val publicKey = PUBLIC_KEY_STRING.hexToByteArray()
            ENCODED = Arrays.copyOf(privateKey, privateKey.size + publicKey.size)
            System.arraycopy(publicKey, 0, ENCODED, privateKey.size, publicKey.size)
        }
    }
}
