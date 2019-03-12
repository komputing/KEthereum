package org.kethereum.crypto

import org.kethereum.crypto.impl.BouncyCastleCryptoAPIProvider
import org.kethereum.crypto.test_data.ADDRESS
import org.kethereum.crypto.test_data.PUBLIC_KEY
import org.kethereum.crypto.test_data.PUBLIC_KEY_STRING
import org.kethereum.model.Address
import org.kethereum.model.PublicKey
import kotlin.test.Test
import kotlin.test.assertEquals

class KeysTest {

    @Test
    fun testCreateEcKeyPair() {
        CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider)
        val (privateKey, publicKey) = createEthereumKeyPair()
        assertEquals(publicKey.key.signum(), 1)
        assertEquals(privateKey.key.signum(), 1)
    }

    @Test
    fun testGetAddress() {
        assertEquals(PublicKey(PUBLIC_KEY_STRING).toAddress(), ADDRESS)
    }

    @Test
    fun testGetAddressZeroPaddedAddress() {
        val publicKey = PublicKey(
            "0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22" +
                    "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113"
        )
        assertEquals(publicKey.toAddress(), Address("01c52b08330e05d731e38c856c1043288f7d9744"))
    }

    @Test
    fun testGetAddressBigInteger() {
        assertEquals(PUBLIC_KEY.toAddress(), ADDRESS)
    }

}
