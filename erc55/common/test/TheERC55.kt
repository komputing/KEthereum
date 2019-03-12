package org.kethereum.erc55

import org.kethereum.model.Address
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheERC55 {

    @Test
    fun returnsTrueForValidChecksum() {
        assertTrue(Address("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed")
                .hasValidERC55ChecksumOrNoChecksum())

        assertTrue(Address("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359")
                .hasValidERC55ChecksumOrNoChecksum())
        assertTrue(Address("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB")
                .hasValidERC55ChecksumOrNoChecksum())
        assertTrue(Address("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb")
                .hasValidERC55ChecksumOrNoChecksum())
    }

    @Test
    fun returnsFalseForBadChecksums() {
        assertFalse(Address("0x5AAeb6053F3E94C9b9A09f33669435E7Ef1BeAeD")
                .hasValidERC55ChecksumOrNoChecksum())

        assertFalse(Address("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5D359")
                .hasValidERC55ChecksumOrNoChecksum())
        assertFalse(Address("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8c6FB")
                .hasValidERC55ChecksumOrNoChecksum())
        assertFalse(Address("0xD1220A0cf47c7B9BE7A2E6BA89F429762e7b9ADb")
                .hasValidERC55ChecksumOrNoChecksum())
    }

    @Test
    fun returnsFalseOnStrictAndOneAddressIsUppercase() {
        assertFalse(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".toUpperCase())
                .hasValidERC55Checksum())

        assertFalse(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".toLowerCase())
                .hasValidERC55Checksum())
    }

    @Test
    fun returnsTrueOnNonStrictAllUpper() {
        assertTrue(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".toUpperCase())
                .hasValidERC55ChecksumOrNoChecksum())
    }

    @Test
    fun returnsTrueOnNonStrictAllLower() {
        assertTrue(Address("5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed".toLowerCase())
                .hasValidERC55ChecksumOrNoChecksum())

    }


}