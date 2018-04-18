package org.kethereum.erc831

import org.assertj.core.api.Assertions
import org.junit.Test

class TheERC831Parsing {

    @Test
    fun canParsePrefix() {
        Assertions.assertThat(parseERC681("ethereum:myawesomeprefix-myawesomepayload").prefix).isEqualTo("myawesomeprefix")
    }

    @Test
    fun canParsePayload() {
        Assertions.assertThat(parseERC681("ethereum:myawesomeprefix-myawesomepayload").payload).isEqualTo("myawesomepayload")
    }

    @Test
    fun canParsePayloadWithDashes() {
        Assertions.assertThat(parseERC681("ethereum:myawesomeprefix-my-awesome-payload").payload).isEqualTo("my-awesome-payload")
    }

    @Test
    fun canDealWithDashAfter0x() {
        Assertions.assertThat(parseERC681("ethereum:0x-bar").prefix).isEqualTo(null)
    }
}