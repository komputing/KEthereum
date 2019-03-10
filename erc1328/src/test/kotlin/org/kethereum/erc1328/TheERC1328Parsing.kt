package org.kethereum.erc1328

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TheERC1328Parsing {

    @Test
    fun weCanParseSessionId() {
        Assertions.assertThat(parseERC1328("ethereum:wc-theSessionID@2").topic).isEqualTo("theSessionID")
    }

    @Test
    fun weCanParseVersion() {
        Assertions.assertThat(parseERC1328("ethereum:wc-theSessionID@2?name=foo").version).isEqualTo(2)
    }

    @Test
    fun weCanParseBridge() {
        Assertions.assertThat(parseERC1328("ethereum:wc-theSessionID@4?name=foo&bridge=theBridge").bridge).isEqualTo("theBridge")
    }


    @Test
    fun weCanParseSymKey() {
        Assertions.assertThat(parseERC1328("ethereum:wc-theSessionID@4?name=foo&bridge=theBridge&symKey=theSymKey").symKey).isEqualTo("theSymKey")
    }


}