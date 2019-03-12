package org.kethereum.functions.rlp

import kotlinx.io.core.toByteArray
import org.kethereum.model.extensions.hexToByteArray
import org.kethereum.model.extensions.toByteArray
import org.kethereum.model.extensions.toMinimalByteArray
import org.kethereum.model.extensions.toNoPrefixHexString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TheRLPEncoder {

    @Test
    fun encodingWorks() {
        rlpTestData.forEach { entry ->
            assertEquals(entry.key.encode().toNoPrefixHexString(), entry.value)
        }
    }

    @Test
    fun elementHashCodeIsHashOfArray() {
        testByteArrayHashCode(ByteArray(0))
        testByteArrayHashCode("foo".toByteArray())
        testByteArrayHashCode("Yolo123".toByteArray())
    }

    private fun testByteArrayHashCode(arr: ByteArray) {
        assertEquals(arr.toRLP().hashCode(), arr.hashCode())
    }

    @Test
    fun elementEqualsWorks() {

        assertFalse(RLPElement(ByteArray(0)).equals(null))
        assertFalse(RLPElement("a".toByteArray()).equals("foo"))
        assertFalse(RLPElement("a".toByteArray()).equals("b".toByteArray()))

        assertTrue(RLPElement("a".toByteArray()).equals(RLPElement("a".toByteArray())))
        assertTrue(RLPElement("YoLo".toByteArray()).equals(RLPElement("YoLo".toByteArray())))

    }

    @Test
    fun toByteArrayWorks() {
        assertTrue(0.toByteArray().contentEquals("00000000".hexToByteArray()))
        assertTrue(1.toByteArray().contentEquals("00000001".hexToByteArray()))
        assertTrue(0xab.toByteArray().contentEquals("000000ab".hexToByteArray()))
        assertTrue(0xaabb.toByteArray().contentEquals("0000aabb".hexToByteArray()))
        assertTrue(0xaabbcd.toByteArray().contentEquals("00aabbcd".hexToByteArray()))
        assertTrue(0xaabbcdef.toInt().toByteArray().contentEquals("aabbcdef".hexToByteArray()))
    }

    @Test
    fun toMinimalByteArrayWorks() {
        assertTrue(1.toMinimalByteArray().contentEquals("01".hexToByteArray()))
        assertTrue(0xab.toMinimalByteArray().contentEquals("ab".hexToByteArray()))
        assertTrue(0xaabb.toMinimalByteArray().contentEquals("aabb".hexToByteArray()))
        assertTrue(0xaabbcd.toMinimalByteArray().contentEquals("aabbcd".hexToByteArray()))
        assertTrue(0xaabbcdef.toInt().toMinimalByteArray().contentEquals("aabbcdef".hexToByteArray()))
    }

    @Test
    fun testZeroBytes() {
        val zeroBytes = ByteArray(1) { 0.toByte() }
        val rlp = zeroBytes.toRLP()
        val bytes = rlp.encode()
        val rlp1 = bytes.decodeRLP()
        assertEquals(rlp, rlp1)
    }
}
