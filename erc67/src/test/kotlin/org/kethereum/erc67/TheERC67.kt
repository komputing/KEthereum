package org.kethereum.erc67

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import java.math.BigDecimal
import java.math.BigInteger

class TheERC67 {

    @Test
    fun basicToERC67Works() {
        assertThat(Address("0x00AB42").toERC67String()).isEqualTo("ethereum:0x00AB42")
    }

    @Test
    fun toERC67WithValueWorks() {
        assertThat(Address("0x00AB42").toERC67String(valueInWei = BigInteger("1"))).isEqualTo("ethereum:0x00AB42?value=1")
        assertThat(Address("0x00AB42").toERC67String(valueInWei = BigInteger("2"))).isEqualTo("ethereum:0x00AB42?value=2")

        assertThat(Address("0x00AB42").toERC67String(valueInEther = BigDecimal("0.42"))).isEqualTo("ethereum:0x00AB42?value=420000000000000000")
    }

    @Test
    fun invalidERC67IsDetected() {
        assertThat(ERC67("etherUUm:0x00AB42?value=1").isValid()).isEqualTo(false)
        assertThat(ERC67("ethereum:0x00AB42?value=1a&gas=2").isValid()).isEqualTo(false)
        assertThat(ERC67("ethereum:0x00AB42?value=1&gas=2x").isValid()).isEqualTo(false)
        assertThat(ERC67("ethereum:0x00AB42?value=1?gas=2").isValid()).isEqualTo(false)
    }

    @Test
    fun parsingERC67Works() {
        assertThat(ERC67("ethereum:0x00AB42?value=1").getHex()).isEqualTo("0x00AB42")
        assertThat(ERC67("ethereum:0x00AB42").value).isNull()

        assertThat(ERC67("ethereum:0x00AB42?value=1").isValid()).isEqualTo(true)
        assertThat(ERC67("ethereum:0x00AB42?value=1").value).isEqualTo("1")

        (0..10).forEach {
            val probe = ERC67(Address("0xAABB").toERC67String(valueInWei = BigInteger.valueOf(it.toLong())))
            assertThat(probe.value).isEqualTo(it.toString())
        }
    }


    @Test
    fun parsingERC67WithGasValueWorks() {
        assertThat(ERC67("ethereum:0x00AB42?gas=2").gas).isEqualTo("2")
        assertThat(ERC67("ethereum:0x00AB42?gas=42&value=3").gas).isEqualTo("42")
        assertThat(ERC67("ethereum:0x00AB42?value=1&gas=2").value).isEqualTo("1")
        assertThat(ERC67("ethereum:0x00AB42?gas=2&value=1").gas).isEqualTo("2")
    }

}
