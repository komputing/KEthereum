package org.kethereum.uri.common

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheCommonURIParsing {

    @Test
    fun weCanAccessQuery() {
        Assertions.assertThat(parseCommonURI("ethereum:key-0x00AB42@23?yay").query).isEqualTo(listOf("yay" to "true"))
    }

    @Test
    fun weCanAccessQueryWithFunction() {
        Assertions.assertThat(parseCommonURI("ethereum:key-0x00AB42@23/funfun?yay").query).isEqualTo(listOf("yay" to "true"))
    }

    @Test
    fun weCanHandleEmptyQuery() {
        Assertions.assertThat(parseCommonURI("ethereum:key-0x00AB42@23/funfun").query).isEmpty()
    }

    @Test
    fun weCanHandleQueryWithSameType() {
        Assertions.assertThat(parseCommonURI("ethereum:key-0x00AB42@23/yolo?uint256=123&uint256=2").query.size).isEqualTo(2)
    }

    @Test
    fun orderOfQueryIsPreserved() {
        Assertions.assertThat(parseCommonURI("ethereum:key-0x00AB42@23/funfun?uint8=1&uint8=2").query).isEqualTo(listOf("uint8" to "1","uint8" to "2"))
    }


    @Test
    fun canHandleNegativeQueryWithPrefix() {
        Assertions.assertThat(parseCommonURI("ethereum:foo-0x00AB42/funfun?int8=-42").query).isEqualTo(listOf("int8" to "-42"))
    }


    @Test
    fun canHandleNegativeQueryChain() {
        Assertions.assertThat(parseCommonURI("ethereum:foo-0x00AB42@4/funfun?int8=-42").query).isEqualTo(listOf("int8" to "-42"))
    }

    @Test
    fun weCanParseFunction() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42@23/myfun?value=42e18").function).isEqualTo("myfun")
    }

    @Test
    fun parsingERC681WithChainIdWorks() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42@4?gas=2").chainId).isEqualTo(4)
    }

    @Test
    fun defaultChainIdIsNull() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42?value=42").chainId).isEqualTo(null)
    }

    @Test
    fun canDetectInvalidChainId() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42@failme?value=42").valid).isFalse()
    }

    @Test
    fun garbageIsInvalid() {
        assertThat(parseCommonURI("garbage").valid).isFalse()
    }

    @Test
    fun canParseChainWhenLastElement() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42@42").chainId).isEqualTo(42)
    }

    @Test
    fun canParsePrefix() {
        Assertions.assertThat(parseCommonURI("ethereum:rlp-0x00AB42?value=42").prefix).isEqualTo("rlp")
    }

    @Test
    fun wrongSchemeIsDetected() {
        Assertions.assertThat(parseCommonURI("etherUUm:0x00AB42?value=1").valid).isEqualTo(false)
    }

    @Test
    fun parsingAddressWorks() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42?value=1").address).isEqualTo("0x00AB42")
    }

    @Test
    fun isValidWhenValid() {
        Assertions.assertThat(parseCommonURI("ethereum:0x00AB42?value=1").valid).isEqualTo(true)
    }

}