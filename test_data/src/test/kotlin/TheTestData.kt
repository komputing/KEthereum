import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.getABIString
import kotlin.test.assertNotNull

class TheTestData {

    @Test
    fun canGetTestData() {
        assertNotNull(getABIString("peepeth"))
    }

}