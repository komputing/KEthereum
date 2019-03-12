package org.kethereum.erc961

import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class TheERC961Generator {

    private val testAddress = Address("0x00AB42")
    private val testChain = ChainDefinition(4)
    private val baseToken = Token("TST", testAddress, testChain)

    @Test
    fun basicTo961Works() {
        assertEquals(baseToken.generateURL(), "ethereum:token_info-0x00AB42@4?symbol=TST")
    }


    @Test
    fun canHaveName() {
        assertEquals(
            baseToken.copy(name = "yolo").generateURL(),
            "ethereum:token_info-0x00AB42@4?symbol=TST&name=yolo"
        )
    }

    @Test
    fun canHaveDecimals() {
        assertEquals(
            baseToken.copy(decimals = 0).generateURL(),
            "ethereum:token_info-0x00AB42@4?symbol=TST&decimals=0"
        )
    }

    @Test
    fun canHaveType() {
        assertEquals(
            baseToken.copy(type = "ERC20").generateURL(),
            "ethereum:token_info-0x00AB42@4?symbol=TST&type=ERC20"
        )
    }

    @Test
    fun canHaveAll() {
        assertEquals(
            baseToken.copy(decimals = 2, name = "myName", type = "ERC233").generateURL(),
            "ethereum:token_info-0x00AB42@4?symbol=TST&decimals=2&name=myName&type=ERC233"
        )
    }
}
