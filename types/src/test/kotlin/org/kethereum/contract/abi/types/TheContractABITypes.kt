package org.kethereum.contract.abi.types

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.kethereum.contract.abi.types.model.ContractABITypeDefinition
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.model.types.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

class TheContractABITypes {

    @Test
    fun canDealWithAllKnownTypes() {
        listOf("bool", "string", "bytes20", "uint256", "uint96", "int8", "uint192", "uint32", "uint16", "bytes15", "bytes14", "address", "bytes12", "bytes32", "bytes5", "uint80", "bytes3", "bytes4", "uint224", "int256", "uint128", "int32", "bytes", "bytes8", "uint88", "uint64", "bytes1", "uint8", "bytes2", "uint24", "bytes16", "uint", "int", "byte").forEach {
            assertThat(convertStringToABIType(it)).isInstanceOf(ContractABITypeDefinition::class.java)
        }
    }

    @Test
    fun rejectsInvalidTypes() {
        listOf("bool123", "uint257", "uint-1", "bytes-1", "yolo").forEach {
            assertThrows(IllegalArgumentException::class.java) {
                convertStringToABIType(it)
            }
        }
    }


    @Test
    fun detectsSigned() {
        assertThat(convertStringToABIType("uint8").ethTypeKClass).isEqualTo(UIntETHType::class)
        assertThat(convertStringToABIType("int8").ethTypeKClass).isEqualTo(IntETHType::class)
    }

    @Test
    fun bitsExtractedCorrectly() {
        assertThat(convertStringToABIType("uint8").params).isEqualTo(BitsTypeParams(8))
        assertThat(convertStringToABIType("int8").params).isEqualTo(BitsTypeParams(8))
        assertThat(convertStringToABIType("uint16").params).isEqualTo(BitsTypeParams(16))
        assertThat(convertStringToABIType("int16").params).isEqualTo(BitsTypeParams(16))
    }

    @Test
    fun bytesExtractedCorrectly() {
        assertThat(convertStringToABIType("bytes23").params).isEqualTo(BytesTypeParams(23))
    }


    @Test
    fun unsignedIntABITypeWillNotAcceptNegativeNumber() {
        assertThrows(IllegalArgumentException::class.java) {
            UIntETHType.ofSting("-1", BitsTypeParams(8))
        }
    }

    @Test
    fun acceptsCorrectAddress() {
        val address = "0xbE27686a93c54Af2f55f16e8dE9E6Dc5dccE915e"

        val myAddress = AddressETHType.ofString(address)

        assertThat(myAddress.paddedValue).isEqualTo(ByteArray(12) + HexString(address).hexToByteArray())
    }

    @Test
    fun rejectsInvalidAddress() {
        assertThrows(IllegalArgumentException::class.java) {
            AddressETHType.ofString("0x123")
        }

        assertThrows(IllegalArgumentException::class.java) {
            AddressETHType.ofString("0xbE27686a93c54Af2f55f16e8dE9E6Dc5dccE915e123")
        }

        assertThrows(IllegalArgumentException::class.java) {
            AddressETHType.ofString("0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        }
    }

    @Test
    fun rejectsInvalidBools() {
        assertThrows(IllegalArgumentException::class.java) {
            BoolETHType.ofString("0x123")
        }

        assertThrows(IllegalArgumentException::class.java) {
            BoolETHType.ofString("yolo")
        }

        assertThrows(IllegalArgumentException::class.java) {
            BoolETHType.ofString("truewithdirt")
        }
    }

    @Test
    fun handlesValidBools() {
        assertThat(BoolETHType.ofString("true").paddedValue)
                .isEqualTo(HexString("0x" + "0".repeat(63) + "1").hexToByteArray())

        assertThat(BoolETHType.ofString("TRUE").paddedValue)
                .isEqualTo(HexString("0x" + "0".repeat(63) + "1").hexToByteArray())

        assertThat(BoolETHType.ofString("false").paddedValue)
                .isEqualTo(HexString("0x" + "0".repeat(64)).hexToByteArray())

        assertThat(BoolETHType.ofString("FALSE").paddedValue)
                .isEqualTo(HexString("0x" + "0".repeat(64)).hexToByteArray())
    }

    @Test
    fun handlesBytes() {
        assertThat(BytesETHType.ofString("0xFA", BytesTypeParams(1)).paddedValue)
                .isEqualTo(HexString("0xFA" + "0".repeat(62)).hexToByteArray())

    }
}