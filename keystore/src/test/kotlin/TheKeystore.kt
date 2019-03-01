
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kethereum.crypto.createEthereumKeyPair
import org.kethereum.crypto.toAddress
import org.kethereum.keystore.api.InitializingFileKeyStore
import org.kethereum.wallet.model.InvalidPasswordException
import java.io.File
import kotlin.test.assertFailsWith

class TheKeystore {

    val key1 = createEthereumKeyPair()
    val key2 = createEthereumKeyPair()

    val keyStoreDir = File("foo")

    @BeforeEach
    fun delete() {
        keyStoreDir.deleteRecursively()
    }

    @Test
    fun startsEmpty() {
        assertThat(InitializingFileKeyStore(keyStoreDir).getAddresses()).isEmpty()
    }

    @Test
    fun canInsertKey() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        assertThat(tested.getAddresses().size).isEqualTo(1)
    }


    @Test
    fun whenAddingTheSameKeyTwiceWeHaveOneInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        tested.addKey(key1, "yolo")
        assertThat(tested.getAddresses().size).isEqualTo(1)
    }

    @Test
    fun whenAddingTwoKeysWeHaveTwoInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        tested.addKey(key2, "pwd")
        assertThat(tested.getAddresses().size).isEqualTo(2)
    }

    @Test
    fun weCanDeleteAKey() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        tested.deleteKey(tested.getAddresses().first())
        assertThat(tested.getAddresses()).isEmpty()
    }

    @Test
    fun hasKeyReturnsTrueForAddressInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        assertThat(tested.hasKeyForForAddress(tested.getAddresses().first())).isTrue()
    }


    @Test
    fun weCanGetTheKey() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        assertThat(tested.getKeyForAddress(tested.getAddresses().first(),"pwd")).isEqualTo(key1)
    }


    @Test
    fun rejectsWrongPassword() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        tested.addKey(key1, "pwd")
        assertFailsWith<InvalidPasswordException> {
            tested.getKeyForAddress(tested.getAddresses().first(), "foo")
        }
    }

    @Test
    fun hasKeyReturnsFalseForAddressNotInStore() {
        val tested = InitializingFileKeyStore(keyStoreDir)
        assertThat(tested.hasKeyForForAddress(key2.toAddress())).isFalse()
    }
}
