package org.kethereum.erc55

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import java.util.*

class TheERC55 {

    @Test
    fun returnsTrueForValidChecksum() {
        assertThat(Address("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed")
                .hasValidERC55ChecksumOrNoChecksum()).isTrue

        assertThat(Address("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359")
                .hasValidERC55ChecksumOrNoChecksum()).isTrue
        assertThat(Address("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB")
                .hasValidERC55ChecksumOrNoChecksum()).isTrue
        assertThat(Address("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb")
                .hasValidERC55ChecksumOrNoChecksum()).isTrue
    }

    @Test
    fun returnsFalseForBadChecksums() {
        assertThat(Address("0x5AAeb6053F3E94C9b9A09f33669435E7Ef1BeAeD")
                .hasValidERC55ChecksumOrNoChecksum()).isFalse

        assertThat(Address("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5D359")
                .hasValidERC55ChecksumOrNoChecksum()).isFalse
        assertThat(Address("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8c6FB")
                .hasValidERC55ChecksumOrNoChecksum()).isFalse
        assertThat(Address("0xD1220A0cf47c7B9BE7A2E6BA89F429762e7b9ADb")
                .hasValidERC55ChecksumOrNoChecksum()).isFalse
    }

    @Test
    fun returnsFalseOnStrictAndOneAddressIsUppercase() {
        assertThat(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".uppercase())
                .hasValidERC55Checksum()).isFalse

        assertThat(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".lowercase())
                .hasValidERC55Checksum()).isFalse
    }

    @Test
    fun returnsTrueOnNonStrictAllUpper() {
        assertThat(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".uppercase())
                .hasValidERC55ChecksumOrNoChecksum()).isTrue
    }

    @Test
    fun returnsTrueOnNonStrictAllLower() {
        assertThat(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".uppercase())
                .hasValidERC55ChecksumOrNoChecksum()).isTrue

    }


}