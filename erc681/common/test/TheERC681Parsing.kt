package org.kethereum.erc681

import org.kethereum.model.number.BigInteger
import kotlin.test.*

class TheERC681Parsing {

    @Test
    fun weCanAccessFunctionParams() {
        assertEquals(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3&yay").functionParams, listOf("yay" to "true"))
    }

    @Test
    fun weCanAccessFunctionParamsWithFunction() {
        assertEquals(parseERC681("ethereum:pay-0x00AB42@23/funfun?value=42&gas=3&yay").functionParams, listOf("yay" to "true"))
    }

    @Test
    fun weCanHandleEmptyFunctionParams() {
        assertTrue(parseERC681("ethereum:pay-0x00AB42@23/funfun").functionParams.isEmpty())
    }

    @Test
    fun weCanHandleFunctionParamsWithSameType() {
        assertEquals(parseERC681("ethereum:pay-0x00AB42@23/yolo?uint256=123&uint256=2").functionParams.size, 2)
    }

    @Test
    fun orderOfFunctionParamsIsPreserved() {
        assertEquals(
            parseERC681("ethereum:pay-0x00AB42@23/funfun?value=42&gas=3&uint8=1&uint8=2").functionParams,
            listOf("uint8" to "1","uint8" to "2")
        )
    }


    @Test
    fun canHandleNegativeFunctionParameterWithPrefix() {
        assertEquals(
            parseERC681("ethereum:pay-0x00AB42/funfun?value=42&gas=3&int8=-42").functionParams,
            listOf("int8" to "-42")
        )
    }


    @Test
    fun canHandleNegativeFunctionParameterWithChain() {
        assertEquals(
            parseERC681("ethereum:pay-0x00AB42@4/funfun?value=42&gas=3&int8=-42").functionParams,
            listOf("int8" to "-42")
        )
    }


    @Test
    fun canHandleNegativeFunctionParametersOnlyFunction() {
        assertEquals(
            parseERC681("ethereum:0x00AB42/funfun?value=42&gas=3&int8=-42").functionParams,
            listOf("int8" to "-42")
        )
    }

    @Test
    fun weCanParseScientificNotationForValue() {
        assertEquals(parseERC681("ethereum:0x00AB42@23?value=42e18").value, BigInteger("42000000000000000000"))
    }

    @Test
    fun weCanParseScientificNotationWithDecimalsForValue() {
        assertEquals(parseERC681("ethereum:0x00AB42@23?value=42.123e18").value, BigInteger("42123000000000000000"))
    }

    @Test
    fun weCanParseScientificNotationForGas() {
        assertEquals(parseERC681("ethereum:0x00AB42@23?gas=42e1").gas, BigInteger("420"))
    }

    @Test
    fun weCanParseFunction() {
        assertEquals(parseERC681("ethereum:0x00AB42@23/myfun?value=42e18").function, "myfun")
    }

    @Test
    fun parsingERC681WithChainIdWorks() {
        assertEquals(parseERC681("ethereum:0x00AB42@4?gas=2").chainId, 4)
    }

    @Test
    fun defaultChainIdIsNull() {
        assertNull(parseERC681("ethereum:0x00AB42?value=42").chainId)
    }

    @Test
    fun canDetectInvalidChainId() {
        assertFalse(parseERC681("ethereum:0x00AB42@failme?value=42").valid)
    }

    @Test
    fun garbageIsInvalid() {
        assertFalse(parseERC681("garbage").valid)
    }

    @Test
    fun canParseChainWhenLastElement() {
        assertEquals(parseERC681("ethereum:0x00AB42@42").chainId, 42)
    }

    @Test
    fun invalidERC681IsDetected() {
        assertFalse(parseERC681("etherUUm:0x00AB42?value=1").valid)
        assertFalse(parseERC681("ethereum:0x00AB42?value=1a&gas=2").valid)
        assertFalse(parseERC681("ethereum:0x00AB42?value=1&gas=2x").valid)
        assertFalse(parseERC681("ethereum:0x00AB42?value=1?gas=2").valid)
    }

    @Test
    fun parsingERC681Works() {
        assertEquals(parseERC681("ethereum:0x00AB42?value=1").functionParams, emptyList())
        assertEquals(parseERC681("ethereum:0x00AB42?value=1").address, "0x00AB42")
        assertNull(parseERC681("ethereum:0x00AB42").value)

        assertEquals(parseERC681("ethereum:0x00AB42?value=1").valid, true)
        assertEquals(parseERC681("ethereum:0x00AB42?value=1").value, BigInteger("1"))

        (0..10).forEach {
            val erC681 = ERC681(address = "0xAABB", value = BigInteger.valueOf(it.toLong()))
            val probe = parseERC681(erC681.generateURL())
            assertEquals(probe.value, BigInteger(it.toString()))
        }
    }

    @Test
    fun parsingERC681WithGasValueWorks() {
        assertEquals(parseERC681("ethereum:0x00AB42?gas=2").gas, BigInteger("2"))
        assertEquals(parseERC681("ethereum:0x00AB42?gas=42&value=3").gas, BigInteger("42"))
        assertEquals(parseERC681("ethereum:0x00AB42?value=1&gas=2").value, BigInteger("1"))
        assertEquals(parseERC681("ethereum:0x00AB42?gas=2&value=1").gas, BigInteger("2"))
    }

    @Test
    fun canParseFullyFeaturedERC681URL() {
        assertEquals(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3").prefix, "pay")
        assertEquals(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3").value, BigInteger("42"))
        assertEquals(parseERC681("ethereum:pay-0x00AB42@23?value=42&gas=3").gas, BigInteger("3"))
    }

    @Test
    fun weDoNotStumbleUponIllegal681() {
        assertFalse(parseERC681("ethereum:pay-0x00AB42@23?value=42.42").valid)
    }
}