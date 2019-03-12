package org.kethereum.erc831

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class TheERC831Parsing {

    @Test
    fun canParsePrefix() {
        assertEquals(parseERC831("ethereum:myawesomeprefix-myawesomepayload").prefix, "myawesomeprefix")
    }

    @Test
    fun canParsePayload() {
        assertEquals(parseERC831("ethereum:myawesomeprefix-myawesomepayload").payload, "myawesomepayload")
    }

    @Test
    fun canParsePayloadWithDashes() {
        assertEquals(parseERC831("ethereum:myawesomeprefix-my-awesome-payload").payload, "my-awesome-payload")
    }

    @Test
    fun canDealWithDashAfter0x() {
        assertNull(parseERC831("ethereum:0x-bar").prefix)
    }
}