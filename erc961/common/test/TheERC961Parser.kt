package org.kethereum.erc961

import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TheERC961Parser {

    @Test
    fun canParseAddress() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").address, Address("0x00AB42"))
    }

    @Test
    fun canParseChain() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").chain, ChainDefinition(4))
    }

    @Test
    fun canParseSymbol() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?symbol=TST").symbol, "TST")
    }

    @Test
    fun decimalsDefaultTo18() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4").decimals, 18)
    }

    @Test
    fun canParseDecimals() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?decimals=5").decimals, 5)
    }

    @Test
    fun canParseName() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?name=yolo").name, "yolo")
    }

    @Test
    fun canParseType() {
        assertEquals(parseTokenFromEthereumURI("ethereum:token_info-0x00AB42@4?type=FOO").type, "FOO")
    }

    @Test
    fun canParseFull() {
        val parseTokenFromEthereumURI = parseTokenFromEthereumURI("ethereum:token_info-0x00AB43@42?type=TypTest&name=NameTest&decimals=2&symbol=SymbolTest")
        assertEquals(parseTokenFromEthereumURI.address, Address("0x00AB43"))
        assertEquals(parseTokenFromEthereumURI.chain, ChainDefinition(42))
        assertEquals(parseTokenFromEthereumURI.type, "TypTest")
        assertEquals(parseTokenFromEthereumURI.name, "NameTest")
        assertEquals(parseTokenFromEthereumURI.symbol, "SymbolTest")
        assertEquals(parseTokenFromEthereumURI.decimals, 2)
    }


    @Test
    fun failsForInvalidScheme() {
        assertFailsWith(InvalidTokenURIException::class) {
            parseTokenFromEthereumURI("ethereUUm:token_info")
        }
    }

}
