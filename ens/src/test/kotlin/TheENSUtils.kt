import org.junit.jupiter.api.Test
import org.kethereum.eip137.ENSName
import org.kethereum.ens.isENSDomain
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheENSUtils {
    @Test
    fun testIsENSDomain() {
        assertTrue(ENSName("foo.eth").isENSDomain())
        assertTrue(ENSName("foo.bar.eth").isENSDomain())
        assertTrue(ENSName("foo.xyz").isENSDomain())
        assertTrue(ENSName("foo.luxe").isENSDomain())

        assertFalse(ENSName("foo.lux").isENSDomain())
        assertFalse(ENSName("yolo.eh").isENSDomain())
        assertFalse(ENSName("yolo.x.yz").isENSDomain())
    }
}
