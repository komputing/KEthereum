package org.kethereum.erc831

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TheERC831Parsing {

    @Test
    fun canParsePrefix() {
        Assertions.assertThat(parseERC831("ethereum:myawesomeprefix-myawesomepayload").prefix).isEqualTo("myawesomeprefix")
    }

    @Test
    fun canParsePayload() {
        Assertions.assertThat(parseERC831("ethereum:myawesomeprefix-myawesomepayload").payload).isEqualTo("myawesomepayload")
    }

    @Test
    fun canParsePayloadWithDashes() {
        Assertions.assertThat(parseERC831("ethereum:myawesomeprefix-my-awesome-payload").payload).isEqualTo("my-awesome-payload")
    }

    @Test
    fun canDealWithDashAfter0x() {
        Assertions.assertThat(parseERC831("ethereum:0x-bar").prefix).isEqualTo(null)
    }
}