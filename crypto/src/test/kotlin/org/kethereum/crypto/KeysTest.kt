package org.kethereum.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.ADDRESS
import org.kethereum.crypto.test_data.PUBLIC_KEY
import org.kethereum.crypto.test_data.PUBLIC_KEY_STRING
import org.kethereum.model.Address
import org.kethereum.model.PublicKey
import org.komputing.khex.model.HexString

class KeysTest {

    @Test
    fun testCreateEcKeyPair() {
        val (privateKey, publicKey) = createEthereumKeyPair()
        assertThat(publicKey.key.signum()).isEqualTo(1)
        assertThat(privateKey.key.signum()).isEqualTo(1)
    }

    @Test
    fun testGetAddress() {
        assertThat(PublicKey(HexString(PUBLIC_KEY_STRING)).toAddress()).isEqualTo(ADDRESS)
    }

    @Test
    fun testGetAddressZeroPaddedAddress() {
        val publicKey =
                PublicKey(HexString("0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22" + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113"))
        assertThat(publicKey.toAddress()).isEqualTo(Address("01c52b08330e05d731e38c856c1043288f7d9744"))
    }

    @Test
    fun testGetAddressBigInteger() {
        assertThat(PUBLIC_KEY.toAddress()).isEqualTo(ADDRESS)
    }

}
