package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.kethereum.crypto.data.ADDRESS
import org.kethereum.crypto.data.PUBLIC_KEY
import org.kethereum.crypto.data.PUBLIC_KEY_STRING
import org.kethereum.crypto.model.PublicKey
import org.kethereum.model.Address

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

        assertThat(privateKey.encoded.size).isEqualTo(144)
        assertThat(publicKey.encoded.size).isEqualTo(88)
    }

    @Test
    fun testCreateEcKeyPair() {
        val (privateKey, publicKey) = createEthereumKeyPair()
        assertThat(publicKey.key.signum()).isEqualTo(1)
        assertThat(privateKey.key.signum()).isEqualTo(1)
    }

    @Test
    fun testGetAddress() {
        assertThat(PublicKey(PUBLIC_KEY_STRING).toAddress()).isEqualTo(ADDRESS)
    }

    @Test
    fun testGetAddressZeroPaddedAddress() {
        val publicKey = PublicKey("0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22" + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113")
        assertThat(publicKey.toAddress()).isEqualTo(Address("01c52b08330e05d731e38c856c1043288f7d9744"))
    }

    @Test
    fun testGetAddressBigInteger() {
        assertThat(PUBLIC_KEY.toAddress()).isEqualTo(ADDRESS)
    }

}
