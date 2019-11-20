import org.junit.jupiter.api.Test
import org.kethereum.eip137.ENSName
import org.kethereum.ens.isPotentialENSDomain
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheENSUtils {
    @Test
    fun testIsENSDomain() {
        assertTrue(ENSName("foo.eth").isPotentialENSDomain())
        assertTrue(ENSName("foo.bar.eth").isPotentialENSDomain())
        assertTrue(ENSName("foo.xyz").isPotentialENSDomain())
        assertTrue(ENSName("foo.luxe").isPotentialENSDomain())

        assertFalse(ENSName("foo.lux").isPotentialENSDomain())
        assertFalse(ENSName("yolo.eh").isPotentialENSDomain())
        assertFalse(ENSName("yolo.x.yz").isPotentialENSDomain())
    }
}
