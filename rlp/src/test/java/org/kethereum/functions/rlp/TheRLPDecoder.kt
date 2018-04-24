package org.kethereum.functions.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.walleth.khex.hexToByteArray

class TheRLPDecoder {

    @Test
    fun convertingWorks() {

        rlpTestData.forEach { rlp, hex ->
            assertThat(hex.hexToByteArray().decodeRLP()).isEqualTo(rlp)
        }
    }

}
