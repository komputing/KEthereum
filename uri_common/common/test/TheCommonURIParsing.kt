package org.kethereum.uri.common

import kotlin.test.*


class TheCommonURIParsing {

    @Test
    fun weCanAccessQuery() {
        assertEquals(parseCommonURI("ethereum:key-0x00AB42@23?yay").query, listOf("yay" to "true"))
    }

    @Test
    fun weCanAccessQueryWithFunction() {
        assertEquals(parseCommonURI("ethereum:key-0x00AB42@23/funfun?yay").query, listOf("yay" to "true"))
    }

    @Test
    fun weCanHandleEmptyQuery() {
        assertTrue(parseCommonURI("ethereum:key-0x00AB42@23/funfun").query.isEmpty())
    }

    @Test
    fun weCanHandleQueryWithSameType() {
        assertEquals(parseCommonURI("ethereum:key-0x00AB42@23/yolo?uint256=123&uint256=2").query.size, 2)
    }

    @Test
    fun orderOfQueryIsPreserved() {
        assertEquals(parseCommonURI("ethereum:key-0x00AB42@23/funfun?uint8=1&uint8=2").query, listOf("uint8" to "1","uint8" to "2"))
    }


    @Test
    fun canHandleNegativeQueryWithPrefix() {
        assertEquals(parseCommonURI("ethereum:foo-0x00AB42/funfun?int8=-42").query, listOf("int8" to "-42"))
    }


    @Test
    fun canHandleNegativeQueryChain() {
        assertEquals(parseCommonURI("ethereum:foo-0x00AB42@4/funfun?int8=-42").query, listOf("int8" to "-42"))
    }

    @Test
    fun weCanParseFunction() {
        assertEquals(parseCommonURI("ethereum:0x00AB42@23/myfun?value=42e18").function, "myfun")
    }

    @Test
    fun parsingERC681WithChainIdWorks() {
        assertEquals(parseCommonURI("ethereum:0x00AB42@4?gas=2").chainId, 4)
    }

    @Test
    fun defaultChainIdIsNull() {
        assertNull(parseCommonURI("ethereum:0x00AB42?value=42").chainId)
    }

    @Test
    fun canDetectInvalidChainId() {
        assertFalse(parseCommonURI("ethereum:0x00AB42@failme?value=42").valid)
    }

    @Test
    fun garbageIsInvalid() {
        assertFalse(parseCommonURI("garbage").valid)
    }

    @Test
    fun canParseChainWhenLastElement() {
        assertEquals(parseCommonURI("ethereum:0x00AB42@42").chainId, 42)
    }

    @Test
    fun canParsePrefix() {
        assertEquals(parseCommonURI("ethereum:rlp-0x00AB42?value=42").prefix, "rlp")
    }

    @Test
    fun wrongSchemeIsDetected() {
        assertFalse(parseCommonURI("etherUUm:0x00AB42?value=1").valid)
    }

    @Test
    fun parsingAddressWorks() {
        assertEquals(parseCommonURI("ethereum:0x00AB42?value=1").address, "0x00AB42")
    }

    @Test
    fun isValidWhenValid() {
        assertTrue(parseCommonURI("ethereum:0x00AB42?value=1").valid)
    }

}