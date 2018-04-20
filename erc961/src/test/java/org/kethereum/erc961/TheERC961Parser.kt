package org.kethereum.erc961

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition

class TheERC961Parser {

    @Test
    fun canParseAddress() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").address)
                .isEqualTo(Address("0x00AB42"))
    }

    @Test
    fun canParseChain() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").chain)
                .isEqualTo(ChainDefinition(4))
    }

    @Test
    fun canParseSymbol() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").symbol)
                .isEqualTo("TST")
    }

    @Test
    fun decimalsDefaultTo18() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4").decimals)
                .isEqualTo(18)
    }

    @Test
    fun canParseDecimals() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?decimals=5").decimals)
                .isEqualTo(5)
    }

    @Test
    fun canParseName() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?name=yolo").name)
                .isEqualTo("yolo")
    }

    @Test
    fun canParseType() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?type=FOO").type)
                .isEqualTo("FOO")
    }

    @Test
    fun canParseFull() {
        val parseTokenFromEthereumURI = parseTokenFromEthereumURI("ethereum:token_info-0x00AB43@42?type=TypTest&name=NameTest&decimals=2&symbol=SymbolTest")
        assertThat(parseTokenFromEthereumURI.address).isEqualTo(Address("0x00AB43"))
        assertThat(parseTokenFromEthereumURI.chain).isEqualTo(ChainDefinition(42))
        assertThat(parseTokenFromEthereumURI.type).isEqualTo("TypTest")
        assertThat(parseTokenFromEthereumURI.name).isEqualTo("NameTest")
        assertThat(parseTokenFromEthereumURI.symbol).isEqualTo("SymbolTest")
        assertThat(parseTokenFromEthereumURI.decimals).isEqualTo(2)
    }


    @Test(expected = InvalidTokenURIException::class)
    fun failsForInvalidScheme() {
        parseTokenFromEthereumURI("ethereUUm:token_info")
    }

}
