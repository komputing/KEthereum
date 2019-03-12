package org.kethereum.erc67

import org.kethereum.model.Address
import org.kethereum.model.number.BigDecimal
import org.kethereum.model.number.BigInteger
import kotlin.test.*

class TheERC67 {

    @Test
    fun basicToERC67Works() {
        assertEquals(Address("0x00AB42").toERC67String(), "ethereum:0x00AB42")
    }

    @Test
    fun toERC67WithValueWorks() {
        assertEquals(Address("0x00AB42").toERC67String(valueInWei = BigInteger("1")), "ethereum:0x00AB42?value=1")
        assertEquals(Address("0x00AB42").toERC67String(valueInWei = BigInteger("2")), "ethereum:0x00AB42?value=2")
        assertEquals(Address("0x00AB42").toERC67String(valueInEther = BigDecimal("0.42")), "ethereum:0x00AB42?value=420000000000000000")
    }

    @Test
    fun invalidERC67IsDetected() {
        assertFalse(ERC67("etherUUm:0x00AB42?value=1").isValid())
        assertFalse(ERC67("ethereum:0x00AB42?value=1a&gas=2").isValid())
        assertFalse(ERC67("ethereum:0x00AB42?value=1&gas=2x").isValid())
        assertFalse(ERC67("ethereum:0x00AB42?value=1?gas=2").isValid())
    }

    @Test
    fun parsingERC67Works() {
        assertEquals(ERC67("ethereum:0x00AB42?value=1").getHex(), "0x00AB42")
        assertNull(ERC67("ethereum:0x00AB42").value)

        assertTrue(ERC67("ethereum:0x00AB42?value=1").isValid())
        assertEquals(ERC67("ethereum:0x00AB42?value=1").value, "1")

        (0..10).forEach {
            val probe = ERC67(Address("0xAABB").toERC67String(valueInWei = BigInteger.valueOf(it.toLong())))
            assertEquals(probe.value, it.toString())
        }
    }


    @Test
    fun parsingERC67WithGasValueWorks() {
        assertEquals(ERC67("ethereum:0x00AB42?gas=2").gas, "2")
        assertEquals(ERC67("ethereum:0x00AB42?gas=42&value=3").gas, "42")
        assertEquals(ERC67("ethereum:0x00AB42?value=1&gas=2").value, "1")
        assertEquals(ERC67("ethereum:0x00AB42?gas=2&value=1").gas, "2")
    }

}
