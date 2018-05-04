package org.kethereum.contract.abi.types

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.contract.abi.types.model.*
import org.walleth.khex.hexToByteArray
import kotlin.test.assertFailsWith

class TheContractABITypes {

    @Test
    fun canDealWithAllKnownTypes() {
        // all currently used types as collected via 4byte directory
        listOf("bool", "string", "bytes20", "uint256", "uint96", "int8", "uint192", "uint32", "uint16", "bytes15", "bytes14", "address", "bytes12", "bytes32", "bytes5", "uint80", "bytes3", "bytes4", "uint224", "int256", "uint128", "int32", "bytes", "bytes8", "uint88", "uint64", "bytes1", "uint8", "bytes2", "uint24", "bytes16").forEach {
            assertThat(convertStringToABIType(it)).isInstanceOf(ContractABIType::class.java)
        }
    }

    @Test
    fun rejectsInvalidTypes() {
        listOf("bool123", "uint257", "uint-1", "bytes-1", "yolo").forEach {
            assertFailsWith<IllegalArgumentException> {
                convertStringToABIType(it)
            }
        }
    }


    @Test
    fun detectsSigned() {
        assertThat((convertStringToABIType("uint8") as IntABIType).signed).isFalse()
        assertThat((convertStringToABIType("int8") as IntABIType).signed).isTrue()
    }

    @Test
    fun bitsExtractedCorrectly() {
        assertThat((convertStringToABIType("uint8") as IntABIType).bits).isEqualTo(8)
        assertThat((convertStringToABIType("int8") as IntABIType).bits).isEqualTo(8)
        assertThat((convertStringToABIType("uint16") as IntABIType).bits).isEqualTo(16)
        assertThat((convertStringToABIType("int16") as IntABIType).bits).isEqualTo(16)
    }

    @Test
    fun bytesExtractedCorrectly() {
        assertThat((convertStringToABIType("bytes23") as BytesABIType).bytes).isEqualTo(23)
    }


    @Test
    fun unsignedIntABITypeWillNotAcceptNegativeNumber() {
        assertFailsWith<IllegalArgumentException> {
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

        assertThat(myAddress.toBytes()).isEqualTo(ByteArray(12) + address.hexToByteArray())
    }

    @Test
    fun rejectsInvalidAddress() {
        assertFailsWith<IllegalArgumentException> {
            AddressABIType().parseValueFromString("0x123")
        }

        assertFailsWith<IllegalArgumentException> {
            AddressABIType().parseValueFromString("0xbE27686a93c54Af2f55f16e8dE9E6Dc5dccE915e123")
        }

        assertFailsWith<IllegalArgumentException> {
            AddressABIType().parseValueFromString("0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        }
    }

    @Test
    fun rejectsInvalidBools() {
        assertFailsWith<IllegalArgumentException> {
            BoolABIType().parseValueFromString("0x123")
        }

        assertFailsWith<IllegalArgumentException> {
            BoolABIType().parseValueFromString("yolo")
        }

        assertFailsWith<IllegalArgumentException> {
            BoolABIType().parseValueFromString("truewithdirt")
        }
    }

    @Test
    fun handlesValidBools() {
        assertThat(BoolABIType().apply { parseValueFromString("true") }.toBytes())
                .isEqualTo(("0x" + "0".repeat(63) + "1").hexToByteArray())

        assertThat(BoolABIType().apply { parseValueFromString("TRUE") }.toBytes())
                .isEqualTo(("0x" + "0".repeat(63) + "1").hexToByteArray())

        assertThat(BoolABIType().apply { parseValueFromString("false") }.toBytes())
                .isEqualTo(("0x" + "0".repeat(64)).hexToByteArray())

        assertThat(BoolABIType().apply { parseValueFromString("FALSE") }.toBytes())
                .isEqualTo(("0x" + "0".repeat(64)).hexToByteArray())
    }

    @Test
    fun handlesBytes() {
        assertThat(BytesABIType(1).apply { parseValueFromString("0xFA") }.toBytes())
                .isEqualTo(("0x" + "0".repeat(62) + "FA").hexToByteArray())

    }
}