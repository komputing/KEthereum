package org.kethereum.erc681

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.ChainId
import java.math.BigInteger

class TheERC681Generator {

    @Test
    fun basicToERC681Works() {
        assertThat(ERC681(address = "0x00AB42").generateURL()).isEqualTo("ethereum:0x00AB42")
    }

    @Test
    fun toERC681WithValueWorks() {
        assertThat(ERC681(address = "0x00AB42", value = BigInteger("1")).generateURL()).isEqualTo("ethereum:0x00AB42?value=1")
        assertThat(ERC681(address = "0x00AB42", value = BigInteger("2")).generateURL()).isEqualTo("ethereum:0x00AB42?value=2")
    }

    @Test
    fun toERC681WithChainIdWorks() {
        assertThat(ERC681(address = "0x00AB42", chainId = ChainId(2)).generateURL()).isEqualTo("ethereum:0x00AB42@2")
        assertThat(ERC681(address = "0x00AB42", chainId = ChainId(4)).generateURL()).isEqualTo("ethereum:0x00AB42@4")
    }

    @Test
    fun toERC681WithFunctionWorks() {
        assertThat(ERC681(address = "0x00AB42", function = "funFTW").generateURL()).isEqualTo("ethereum:0x00AB42/funFTW")
        assertThat(ERC681(address = "0x00AB42", function = "yolo").generateURL()).isEqualTo("ethereum:0x00AB42/yolo")
    }

    @Test
    fun toERC681WithFunctionThatHas2ParametersOfTheSameTypeWorks() {
        assertThat(ERC681(address = "0x00AB42", function = "funFTW", functionParams = listOf(Pair("uint256", "2"), Pair("uint256", "1"))).generateURL())
                .isEqualTo("ethereum:0x00AB42/funFTW?uint256=2&uint256=1")
    }

    @Test
    fun toERC681WithPrefixWorks() {
        assertThat(ERC681(address = "0x00AB42", prefix = "prefixFTW").generateURL()).isEqualTo("ethereum:prefixFTW-0x00AB42")
        assertThat(ERC681(address = "0x00AB42", prefix = "yolo").generateURL()).isEqualTo("ethereum:yolo-0x00AB42")
    }

    @Test
    fun toERC681Full() {
        val highUsageERC681 = ERC681(address = "0x00AB42", prefix = "prefixFTW", chainId = ChainId(42), function = "funfun", value = BigInteger("100"), gasLimit = BigInteger("5"),
                functionParams = listOf("uint256" to "0", "address" to "0x0"))
        assertThat(highUsageERC681.generateURL()).isEqualTo("ethereum:prefixFTW-0x00AB42@42/funfun?uint256=0&address=0x0&gas=5&value=100")
        assertThat(highUsageERC681.copy(prefix = null).generateURL()).isEqualTo("ethereum:0x00AB42@42/funfun?uint256=0&address=0x0&gas=5&value=100")
    }
}
