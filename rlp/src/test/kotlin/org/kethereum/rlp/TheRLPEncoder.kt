package org.kethereum.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.extensions.toByteArray
import org.kethereum.extensions.toMinimalByteArray
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toNoPrefixHexString
import org.komputing.khex.model.HexString

class TheRLPEncoder {

    @Test
    fun encodingWorks() {

        rlpTestData.forEach { (rlpElement, hex) ->
            assertThat(rlpElement.encode().toNoPrefixHexString()).isEqualTo(hex)
        }
    }

    @Test
    fun elementHashCodeIsHashOfArray() {
        testByteArrayHashCode(ByteArray(0))
        testByteArrayHashCode("foo".toByteArray())
        testByteArrayHashCode("Yolo123".toByteArray())
    }

    private fun testByteArrayHashCode(arr: ByteArray) {
        assertThat(arr.toRLP().hashCode()).isEqualTo(arr.contentHashCode())
    }

    @Test
    fun elementEqualsWorks() {

        assertThat(RLPElement(ByteArray(0))).isNotEqualTo(null)
        assertThat(RLPElement("a".toByteArray())).isNotEqualTo("foo")
        assertThat(RLPElement("a".toByteArray())).isNotEqualTo("b".toByteArray())

        assertThat(RLPElement("a".toByteArray())).isEqualTo(RLPElement("a".toByteArray()))
        assertThat(RLPElement("YoLo".toByteArray())).isEqualTo(RLPElement("YoLo".toByteArray()))

    }

    @Test
    fun toByteArrayWorks() {
        assertThat(0.toByteArray()).isEqualTo(HexString("00000000").hexToByteArray())
        assertThat(1.toByteArray()).isEqualTo(HexString("00000001").hexToByteArray())
        assertThat(0xab.toByteArray()).isEqualTo(HexString("000000ab").hexToByteArray())
        assertThat(0xaabb.toByteArray()).isEqualTo(HexString("0000aabb").hexToByteArray())
        assertThat(0xaabbcd.toByteArray()).isEqualTo(HexString("00aabbcd").hexToByteArray())
        assertThat(0xaabbcdef.toInt().toByteArray()).isEqualTo(HexString("aabbcdef").hexToByteArray())
    }

    @Test
    fun toMinimalByteArrayWorks() {
        assertThat(1.toMinimalByteArray()).isEqualTo(HexString("01").hexToByteArray())
        assertThat(0xab.toMinimalByteArray()).isEqualTo(HexString("ab").hexToByteArray())
        assertThat(0xaabb.toMinimalByteArray()).isEqualTo(HexString("aabb").hexToByteArray())
        assertThat(0xaabbcd.toMinimalByteArray()).isEqualTo(HexString("aabbcd").hexToByteArray())
        assertThat(0xaabbcdef.toInt().toMinimalByteArray()).isEqualTo(HexString("aabbcdef").hexToByteArray())
    }

    @Test
    fun testZeroBytes() {
        val zeroBytes = ByteArray(1) { 0.toByte() }
        val rlp = zeroBytes.toRLP()
        val bytes = rlp.encode()
        val rlp1 = bytes.decodeRLP()
        assertThat(rlp).isEqualTo(rlp1)
    }
}
