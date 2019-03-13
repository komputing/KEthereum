
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.kethereum.crypto.CryptoAPI
import org.kethereum.crypto.createEthereumKeyPair
import org.kethereum.crypto.impl.BouncyCastleCryptoAPIProvider
import org.kethereum.crypto.toAddress
import org.kethereum.keystore.api.InitializingFileKeyStore
import org.kethereum.wallet.model.InvalidPasswordException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheKeystore {

    private val key1 = CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider).run { createEthereumKeyPair() }
    private val key2 = CryptoAPI.setProvider(BouncyCastleCryptoAPIProvider).run { createEthereumKeyPair() }

    @get:Rule val keyStoreDir = TemporaryFolder()

    @Test
    fun startsEmpty() {
        assertTrue(InitializingFileKeyStore(keyStoreDir.root).getAddresses().isEmpty())
    }

    @Test
    fun canInsertKey() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        assertEquals(tested.getAddresses().size, 1)
    }


    @Test
    fun whenAddingTheSameKeyTwiceWeHaveOneInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        tested.addKey(key1, "yolo")
        assertEquals(tested.getAddresses().size, 1)
    }

    @Test
    fun whenAddingTwoKeysWeHaveTwoInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        tested.addKey(key2, "pwd")
        assertEquals(tested.getAddresses().size, 2)
    }

    @Test
    fun weCanDeleteAKey() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        tested.deleteKey(tested.getAddresses().first())
        assertTrue(tested.getAddresses().isEmpty())
    }

    @Test
    fun hasKeyReturnsTrueForAddressInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        assertTrue(tested.hasKeyForForAddress(tested.getAddresses().first()))
    }


    @Test
    fun weCanGetTheKey() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        assertEquals(tested.getKeyForAddress(tested.getAddresses().first(),"pwd"), key1)
    }


    @Test
    fun rejectsWrongPassword() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        tested.addKey(key1, "pwd")
        assertFailsWith<InvalidPasswordException> {
            tested.getKeyForAddress(tested.getAddresses().first(), "foo")
        }
    }

    @Test
    fun hasKeyReturnsFalseForAddressNotInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir.root)
        assertFalse(tested.hasKeyForForAddress(key2.toAddress()))
    }
}
