package org.kethereum.erc681

import org.kethereum.model.number.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class TheERC681Generator {

    @Test
    fun basicToERC681Works() {
        assertEquals(ERC681(address = "0x00AB42").generateURL(), "ethereum:0x00AB42")
    }

    @Test
    fun toERC681WithValueWorks() {
        assertEquals(ERC681(address = "0x00AB42", value = BigInteger("1")).generateURL(), "ethereum:0x00AB42?value=1")
        assertEquals(ERC681(address = "0x00AB42", value = BigInteger("2")).generateURL(), "ethereum:0x00AB42?value=2")
    }

    @Test
    fun toERC681WithChainIdWorks() {
        assertEquals(ERC681(address = "0x00AB42", chainId = 2L).generateURL(), "ethereum:0x00AB42@2")
        assertEquals(ERC681(address = "0x00AB42", chainId = 4L).generateURL(), "ethereum:0x00AB42@4")
    }

    @Test
    fun toERC681WithFunctionWorks() {
        assertEquals(ERC681(address = "0x00AB42", function = "funFTW").generateURL(), "ethereum:0x00AB42/funFTW")
        assertEquals(ERC681(address = "0x00AB42", function = "yolo").generateURL(), "ethereum:0x00AB42/yolo")
    }

    @Test
    fun toERC681WithFunctionThatHas2ParametersOfTheSameTypeWorks() {
        assertEquals(
            ERC681(address = "0x00AB42", function = "funFTW",functionParams = listOf(Pair("uint256","2"),Pair("uint256","1"))).generateURL(),
            "ethereum:0x00AB42/funFTW?uint256=2&uint256=1"
        )
    }

    @Test
    fun toERC681WithPrefixWorks() {
        assertEquals(ERC681(address = "0x00AB42", prefix = "prefixFTW").generateURL(), "ethereum:prefixFTW-0x00AB42")
        assertEquals(ERC681(address = "0x00AB42", prefix = "yolo").generateURL(), "ethereum:yolo-0x00AB42")
    }

    @Test
    fun toERC681Full() {
        val highUsageERC681 = ERC681(
            address = "0x00AB42",
            prefix = "prefixFTW",
            chainId = 42,
            function = "funfun",
            value = BigInteger("100"),
            gas= BigInteger("5"),
            functionParams = listOf("uint256" to "0", "address" to "0x0")
        )
        assertEquals(highUsageERC681.generateURL(), "ethereum:prefixFTW-0x00AB42@42/funfun?uint256=0&address=0x0&gas=5&value=100")
        assertEquals(highUsageERC681.copy(prefix = null).generateURL(), "ethereum:0x00AB42@42/funfun?uint256=0&address=0x0&gas=5&value=100")
    }
}
