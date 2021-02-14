package org.kethereum.erc681

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.ChainId

class TheERC681Parsing {

    @Test
    fun weCanAccessFunctionParams() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3&yay").functionParams).isEqualTo(listOf("yay" to "true"))
    }

    @Test
    fun weCanAccessFunctionParamsWithFunction() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23/funfun?value=42&gas=3&yay").functionParams).isEqualTo(listOf("yay" to "true"))
    }

    @Test
    fun weCanHandleEmptyFunctionParams() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23/funfun").functionParams).isEmpty()
    }

    @Test
    fun weCanHandleFunctionParamsWithSameType() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23/yolo?uint256=123&uint256=2").functionParams.size).isEqualTo(2)
    }

    @Test
    fun orderOfFunctionParamsIsPreserved() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23/funfun?value=42&gas=3&uint8=1&uint8=2").functionParams).isEqualTo(listOf("uint8" to "1","uint8" to "2"))
    }


    @Test
    fun canHandleNegativeFunctionParameterWithPrefix() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42/funfun?value=42&gas=3&int8=-42").functionParams).isEqualTo(listOf("int8" to "-42"))
    }


    @Test
    fun canHandleNegativeFunctionParameterWithChain() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@4/funfun?value=42&gas=3&int8=-42").functionParams).isEqualTo(listOf("int8" to "-42"))
    }


    @Test
    fun canHandleNegativeFunctionParametersOnlyFunction() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42/funfun?value=42&gas=3&int8=-42").functionParams).isEqualTo(listOf("int8" to "-42"))
    }

    @Test
    fun weCanParseScientificNotationForValue() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23?value=42e18").value).isEqualTo(BigInteger.parseString("42000000000000000000"))
    }

    @Test
    fun weCanParseScientificNotationWithDecimalsForValue() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23?value=42.123e18").value).isEqualTo(BigInteger.parseString("42123000000000000000"))
    }

    @Test
    fun weCanParseScientificNotationForGas() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23?gas=42e1").gasLimit).isEqualTo(BigInteger.parseString("420"))
    }

    @Test
    fun weCanParseFunction() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/myfun?value=42e18").function).isEqualTo("myfun")
    }

    @Test
    fun parsingERC681WithChainIdWorks() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@4?gas=2").chainId).isEqualTo(ChainId(4))
    }

    @Test
    fun defaultChainIdIsNull() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=42").chainId).isEqualTo(null)
    }

    @Test
    fun canDetectInvalidChainId() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@failme?value=42").valid).isFalse()
    }

    @Test
    fun garbageIsInvalid() {
        assertThat(parseERC681("garbage").valid).isFalse()
    }

    @Test
    fun canParseChainWhenLastElement() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@42").chainId).isEqualTo(ChainId(42))
    }

    @Test
    fun invalidERC681IsDetected() {
        Assertions.assertThat(parseERC681("etherUUm:0x00AB42?value=1").valid).isEqualTo(false)
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1a&gas=2").valid).isEqualTo(false)
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1&gas=2x").valid).isEqualTo(false)
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1?gas=2").valid).isEqualTo(false)
    }

    @Test
    fun parsingERC681Works() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1").functionParams).isEqualTo(emptyList<String>())
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1").address).isEqualTo("0x00AB42")
        Assertions.assertThat(parseERC681("ethereum:0x00AB42").value).isNull()

        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1").valid).isEqualTo(true)
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1").value).isEqualTo(BigInteger.ONE)

        (0..10).forEach {
            val erC681 = ERC681(address = "0xAABB", value = BigInteger(it.toLong()))
            val probe = parseERC681(erC681.generateURL())
            Assertions.assertThat(probe.value).isEqualTo(BigInteger(it))
        }
    }

    @Test
    fun parsingERC681WithGasValueWorks() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?gas=2").gasLimit).isEqualTo(BigInteger(2))
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?gasLimit=4").gasLimit).isEqualTo(BigInteger(4))
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?gas=42&value=3").gasLimit).isEqualTo(BigInteger(42))
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?value=1&gas=2").value).isEqualTo(BigInteger.ONE)
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?gas=2&value=1").gasLimit).isEqualTo(BigInteger(2))
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?gasLimit=4").gasPrice).isNull()
        Assertions.assertThat(parseERC681("ethereum:0x00AB42?gasPrice=42").gasPrice).isEqualTo(BigInteger(42))
    }

    @Test
    fun canParseFullyFeaturedERC681URL() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3").prefix).isEqualTo("pay")
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3").value).isEqualTo(BigInteger(42))
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3").gasLimit).isEqualTo(BigInteger(3))
    }

    @Test
    fun weDoNotStumbleUponIllegal681() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23?value=42.42").valid).isEqualTo(false)
    }

    @Test
    fun testSupportsValueSuffix() {
        Assertions.assertThat(parseERC681("ethereum:pay-0x00AB42@23?value=42-ETH").valid).isEqualTo(true)
    }
}