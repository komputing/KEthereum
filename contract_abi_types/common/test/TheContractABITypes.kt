package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.*
import org.kethereum.model.extensions.hexToByteArray
import kotlin.test.*

class TheContractABITypes {

    @Test
    fun canDealWithAllKnownTypes() {
        // all currently used types as collected via 4byte directory
        listOf("bool", "string", "bytes20", "uint256", "uint96", "int8", "uint192", "uint32", "uint16", "bytes15", "bytes14", "address", "bytes12", "bytes32", "bytes5", "uint80", "bytes3", "bytes4", "uint224", "int256", "uint128", "int32", "bytes", "bytes8", "uint88", "uint64", "bytes1", "uint8", "bytes2", "uint24", "bytes16").forEach {
            assertTrue(convertStringToABIType(it) is ContractABIType)
        }
    }

    @Test
    fun rejectsInvalidTypes() {
        listOf("bool123", "uint257", "uint-1", "bytes-1", "yolo").forEach {
            assertFailsWith(IllegalArgumentException::class) {
                convertStringToABIType(it)
            }
        }
    }


    @Test
    fun detectsSigned() {
        assertFalse((convertStringToABIType("uint8") as IntABIType).signed)
        assertTrue((convertStringToABIType("int8") as IntABIType).signed)
    }

    @Test
    fun bitsExtractedCorrectly() {
        assertEquals((convertStringToABIType("uint8") as IntABIType).bits, 8)
        assertEquals((convertStringToABIType("int8") as IntABIType).bits, 8)
        assertEquals((convertStringToABIType("uint16") as IntABIType).bits, 16)
        assertEquals((convertStringToABIType("int16") as IntABIType).bits, 16)
    }

    @Test
    fun bytesExtractedCorrectly() {
        assertEquals((convertStringToABIType("bytes23") as BytesABIType).bytes, 23)
    }


    @Test
    fun unsignedIntABITypeWillNotAcceptNegativeNumber() {
        assertFailsWith(IllegalArgumentException::class) {
            IntABIType(8, false).parseValueFromString("-1")
        }
    }

    @Test
    fun signedIntABITypeWillAcceptNegativeNumber() {
        IntABIType(8, true).parseValueFromString("-1")
    }


    @Test
    fun acceptsCorrectAddress() {
        val address = "0xbE27686a93c54Af2f55f16e8dE9E6Dc5dccE915e"

        val myAddress = AddressABIType().apply {
            parseValueFromString(address)
        }

        assertTrue(myAddress.toBytes().contentEquals(ByteArray(12) + address.hexToByteArray()))
    }

    @Test
    fun rejectsInvalidAddress() {
        assertFailsWith(IllegalArgumentException::class) {
            AddressABIType().parseValueFromString("0x123")
        }

        assertFailsWith(IllegalArgumentException::class) {
            AddressABIType().parseValueFromString("0xbE27686a93c54Af2f55f16e8dE9E6Dc5dccE915e123")
        }

        assertFailsWith(IllegalArgumentException::class) {
            AddressABIType().parseValueFromString("0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        }
    }

    @Test
    fun rejectsInvalidBools() {
        assertFailsWith(IllegalArgumentException::class) {
            BoolABIType().parseValueFromString("0x123")
        }

        assertFailsWith(IllegalArgumentException::class) {
            BoolABIType().parseValueFromString("yolo")
        }

        assertFailsWith(IllegalArgumentException::class) {
            BoolABIType().parseValueFromString("truewithdirt")
        }
    }

    @Test
    fun handlesValidBools() {
        assertTrue(BoolABIType().apply { parseValueFromString("true") }.toBytes()
            .contentEquals(("0x" + "0".repeat(63) + "1").hexToByteArray())
        )

        assertTrue(BoolABIType().apply { parseValueFromString("TRUE") }.toBytes()
            .contentEquals(("0x" + "0".repeat(63) + "1").hexToByteArray())
        )

        assertTrue(BoolABIType().apply { parseValueFromString("false") }.toBytes()
            .contentEquals(("0x" + "0".repeat(64)).hexToByteArray())
        )

        assertTrue(BoolABIType().apply { parseValueFromString("FALSE") }.toBytes()
            .contentEquals(("0x" + "0".repeat(64)).hexToByteArray())
        )
    }

    @Test
    fun handlesBytes() {
        assertTrue(BytesABIType(1).apply { parseValueFromString("0xFA") }.toBytes()
                .contentEquals(("0x" + "0".repeat(62) + "FA").hexToByteArray())
        )
    }
}