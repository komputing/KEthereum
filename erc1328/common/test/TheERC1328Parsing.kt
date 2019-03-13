package org.kethereum.erc1328

import kotlin.test.Test
import kotlin.test.assertEquals

class TheERC1328Parsing {

    @Test
    fun weCanParseSessionId() {
        assertEquals(parseERC1328("ethereum:wc-theSessionID@2").topic, "theSessionID")
    }

    @Test
    fun weCanParseVersion() {
        assertEquals(parseERC1328("ethereum:wc-theSessionID@2?name=foo").version, 2)
    }

    @Test
    fun weCanParseBridge() {
        assertEquals(parseERC1328("ethereum:wc-theSessionID@4?name=foo&bridge=theBridge").bridge, "theBridge")
    }


    @Test
    fun weCanParseSymKey() {
        assertEquals(parseERC1328("ethereum:wc-theSessionID@4?name=foo&bridge=theBridge&symKey=theSymKey").symKey, "theSymKey")
    }
}