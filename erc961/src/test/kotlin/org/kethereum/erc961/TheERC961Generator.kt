package org.kethereum.erc961

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.Token

class TheERC961Generator {

    private val testAddress = Address("0x00AB42")
    private val testChainId = ChainId(4)
    private val baseToken = Token("TST", testAddress, testChainId)

    @Test
    fun basicTo961Works() {
        assertThat(baseToken.generateURL()).isEqualTo("ethereum:token_info-0x00AB42@4?symbol=TST")
    }


    @Test
    fun canHaveName() {
        assertThat(baseToken.copy(name = "yolo").generateURL())
                .isEqualTo("ethereum:token_info-0x00AB42@4?symbol=TST&name=yolo")
    }

    @Test
    fun canHaveDecimals() {
        assertThat(baseToken.copy(decimals = 0).generateURL())
                .isEqualTo("ethereum:token_info-0x00AB42@4?symbol=TST&decimals=0")
    }

    @Test
    fun canHaveType() {
        assertThat(baseToken.copy(type = "ERC20").generateURL())
                .isEqualTo("ethereum:token_info-0x00AB42@4?symbol=TST&type=ERC20")
    }

    @Test
    fun canHaveAll() {
        assertThat(baseToken.copy(decimals = 2, name = "myName", type = "ERC233").generateURL())
                .isEqualTo("ethereum:token_info-0x00AB42@4?symbol=TST&decimals=2&name=myName&type=ERC233")
    }
}
