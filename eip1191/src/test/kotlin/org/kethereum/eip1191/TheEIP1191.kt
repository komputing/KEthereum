package org.kethereum.eip1191

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.ChainId

class TheEIP1191 {

    val rskMainnet = ChainId(30)
    val rskTestnet = ChainId(31)

    @Test
    fun returnsTrueForValidEIP1191(){
        assertThat(Address("0x27b1fdb04752bbc536007a920d24acb045561c26")
                .hasValidEIP1191Checksum(null)).isTrue()
        assertThat(Address("0x27b1FdB04752BBc536007A920D24ACB045561c26")
                .hasValidEIP1191Checksum(rskMainnet)).isTrue()
        assertThat(Address("0x27B1FdB04752BbC536007a920D24acB045561C26")
                .hasValidEIP1191Checksum(rskTestnet)).isTrue()
    }

    @Test
    fun returnsFalseForInvalidEIP1191(){
        assertThat(Address("0x27b1fdb04752bbc536007a920d24acb045561c26")
                .hasValidEIP1191Checksum(rskTestnet)).isFalse()
        assertThat(Address("0x27b1fdb04752bbc536007a920d24acb045561c26")
                .hasValidEIP1191Checksum(rskMainnet)).isFalse()
        assertThat(Address("0x27b1FdB04752BBc536007A920D24ACB045561c26")
                .hasValidEIP1191Checksum(rskTestnet)).isFalse()
        assertThat(Address("0x27B1FdB04752BbC536007a920D24acB045561C26")
                .hasValidEIP1191Checksum(rskMainnet)).isFalse()
    }



}