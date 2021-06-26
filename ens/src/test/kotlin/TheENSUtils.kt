import org.junit.jupiter.api.Test
import org.kethereum.eip137.model.ENSName
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

        assertFalse(ENSName(".").isPotentialENSDomain())
        assertFalse(ENSName("..").isPotentialENSDomain())
        assertFalse(ENSName("a.").isPotentialENSDomain())
        assertFalse(ENSName(".a").isPotentialENSDomain())
    }
}
