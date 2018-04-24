package org.kethereum.functions.rlp

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.extensions.toByteArray
import org.kethereum.extensions.toMinimalByteArray
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toNoPrefixHexString
import java.util.*

class TheRLPEncoder {

    @Test
    fun encodingWorks() {

        rlpTestData.forEach { rlpElement, hex ->
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
        assertThat(arr.toRLP().hashCode()).isEqualTo(Arrays.hashCode(arr))
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
        assertThat(0.toByteArray()).isEqualTo("00000000".hexToByteArray())
        assertThat(1.toByteArray()).isEqualTo("00000001".hexToByteArray())
        assertThat(0xab.toByteArray()).isEqualTo("000000ab".hexToByteArray())
        assertThat(0xaabb.toByteArray()).isEqualTo("0000aabb".hexToByteArray())
        assertThat(0xaabbcd.toByteArray()).isEqualTo("00aabbcd".hexToByteArray())
        assertThat(0xaabbcdef.toInt().toByteArray()).isEqualTo("aabbcdef".hexToByteArray())
    }

    @Test
    fun toMinimalByteArrayWorks() {
        assertThat(1.toMinimalByteArray()).isEqualTo("01".hexToByteArray())
        assertThat(0xab.toMinimalByteArray()).isEqualTo("ab".hexToByteArray())
        assertThat(0xaabb.toMinimalByteArray()).isEqualTo("aabb".hexToByteArray())
        assertThat(0xaabbcd.toMinimalByteArray()).isEqualTo("aabbcd".hexToByteArray())
        assertThat(0xaabbcdef.toInt().toMinimalByteArray()).isEqualTo("aabbcdef".hexToByteArray())
    }
}
