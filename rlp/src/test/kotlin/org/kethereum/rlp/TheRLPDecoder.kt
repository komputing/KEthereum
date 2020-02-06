package org.kethereum.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

class TheRLPDecoder {
    @Test
    fun convertingWorks() {

        rlpTestData.forEach { (rlp, hex) ->
            assertThat(HexString(hex).hexToByteArray().decodeRLP()).isEqualTo(rlp)
        }
    }

    @Test
    fun testRLPException() {

        val hex = "f912345678"

        assertThrows(IllegalRLPException::class.java) {
            HexString(hex).hexToByteArray().decodeRLP()
        }
    }


    @Test
    fun testSizeOverflow() {

        val hex = "f8858088016345785d8a0000a0ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff94095e7baea6a6c7c4c2dfeb977efac326af552d8780801ba048b55bfa915ac795c431978d8a6a992b628d557da5ff759b307d495a36649353a0efffd310ac743f371de3b9f7f9cb56c0b28ad43601b4ab949f53faa07bd2c804"

        assertThat((HexString(hex).hexToByteArray().decodeRLP() as RLPList).element.size).isEqualTo(9)
    }

    @Test
    fun testSizeOverflow2() {

        val hex = "f83f800182520894095e7baea6a6c7c4c2dfeb977efac326af552d870b801ba048b55bfa915ac795c431978d8a6a992b628d557da5ff759b307d495a3664935301"

        assertThat((HexString(hex).hexToByteArray().decodeRLP() as RLPList).element.size).isEqualTo(9)
    }

}
