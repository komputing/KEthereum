package org.kethereum.erc961

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.EthereumURI

class TheERC961Parser {

    @Test
    fun canParseAddress() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").address)
            .isEqualTo(Address("0x00AB42"))
    }

    @Test
    fun canParseAddressAsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST")
        assertThat(address.parseToken().address).isEqualTo(Address("0x00AB42"))
    }

    @Test
    fun canParseChain() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@425?symbol=TST").chain)
            .isEqualTo(ChainId(425))
    }

    @Test
    fun canParseChainAsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@425?symbol=TST")
        assertThat(address.parseToken().chain).isEqualTo(ChainId(425))
    }

    @Test
    fun canParseSymbol() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").symbol)
            .isEqualTo("TST")
    }

    @Test
    fun canParseSymbolAsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST")
        assertThat(address.parseToken().symbol).isEqualTo("TST")
    }

    @Test
    fun decimalsDefaultTo18() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4").decimals)
            .isEqualTo(18)
    }

    @Test
    fun decimalsDefaultTo18AsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@4")
        assertThat(address.parseToken().decimals).isEqualTo(18)
    }

    @Test
    fun canParseDecimals() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?decimals=5").decimals)
            .isEqualTo(5)
    }

    @Test
    fun canParseDecimalsAsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@4?decimals=5")
        assertThat(address.parseToken().decimals).isEqualTo(5)
    }

    @Test
    fun canParseName() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?name=yolo").name)
            .isEqualTo("yolo")
    }

    @Test
    fun canParseNameAsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@4?name=yolo")
        assertThat(address.parseToken().name).isEqualTo("yolo")
    }

    @Test
    fun canParseType() {
        assertThat(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?type=FOO").type)
            .isEqualTo("FOO")
    }

    @Test
    fun canParseTypeAsExtension() {
        val address = EthereumURI("ethereum:token_info-0x00AB42@4?type=FOO")
        assertThat(address.parseToken().type).isEqualTo("FOO")
    }

    @Test
    fun canParseFull() {
        val parseTokenFromEthereumURI =
            parseTokenFromEthereumURI("ethereum:token_info-0x00AB43@425?type=TypTest&name=NameTest&decimals=2&symbol=SymbolTest")
        assertThat(parseTokenFromEthereumURI.address).isEqualTo(Address("0x00AB43"))
        assertThat(parseTokenFromEthereumURI.chain).isEqualTo(ChainId(425))
        assertThat(parseTokenFromEthereumURI.type).isEqualTo("TypTest")
        assertThat(parseTokenFromEthereumURI.name).isEqualTo("NameTest")
        assertThat(parseTokenFromEthereumURI.symbol).isEqualTo("SymbolTest")
        assertThat(parseTokenFromEthereumURI.decimals).isEqualTo(2)
    }

    @Test
    fun failsForInvalidScheme() {
        assertThrows(InvalidTokenURIException::class.java) {
            parseTokenFromEthereumURI("ethereUUm:token_info")
        }
    }
}
