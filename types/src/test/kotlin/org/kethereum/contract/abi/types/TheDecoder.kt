package org.kethereum.contract.abi.types

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.model.types.*
import org.kethereum.crypto.test_data.TEST_ADDRESSES
import org.kethereum.crypto.test_data.TEST_BIGINTEGERS_INCL_NEGATIVE
import org.kethereum.crypto.test_data.TEST_BIGINTEGERS_POSITIVE_ONLY
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

class TheDecoder {

    @Test
    fun testExample1() {
        //https://solidity.readthedocs.io/en/v0.5.12/abi-spec.html#examples
        val paginated = PaginatedByteArray(HexString("00000000000000000000000000000000000000000000000000000000000000450000000000000000000000000000000000000000000000000000000000000001").hexToByteArray())

        assertThat(UIntETHType.ofPaginatedByteArray(paginated, BitsTypeParams(32))?.toKotlinType()).isEqualTo(69)
        assertThat(BoolETHType.ofPaginatedByteArray(paginated)?.toKotlinType()).isTrue()
    }

    @Test
    fun testExample2() {
        //https://solidity.readthedocs.io/en/v0.5.12/abi-spec.html#examples
        val paginated = PaginatedByteArray(HexString("61626300000000000000000000000000000000000000000000000000000000006465660000000000000000000000000000000000000000000000000000000000").hexToByteArray())

        assertThat(BytesETHType.ofPaginatedByteArray(paginated, BytesTypeParams(3))?.toKotlinType()).isEqualTo("abc".toByteArray())
        assertThat(BytesETHType.ofPaginatedByteArray(paginated, BytesTypeParams(3))?.toKotlinType()).isEqualTo("def".toByteArray())
    }

    @Test
    fun testExample3() {
        //https://solidity.readthedocs.io/en/v0.5.12/abi-spec.html#examples
        val paginated = PaginatedByteArray(HexString("0000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000464617665000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000003").hexToByteArray())

        assertThat(DynamicSizedBytesETHType.ofPaginatedByteArray(paginated)?.toKotlinType()).isEqualTo("dave".toByteArray())
        assertThat(BoolETHType.ofPaginatedByteArray(paginated)?.toKotlinType()).isEqualTo(true)
    }

    @Test
    fun thatStringRoundTripWorks() {
        assertThat(StringETHType.ofString("probe").toKotlinType()).isEqualTo("probe")
    }

    @Test
    fun thatStringRoundTripWorksForReallyLongString() {
        assertThat(StringETHType.ofString("probe0000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000464617665000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000003").toKotlinType()).isEqualTo("probe0000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000464617665000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000003")
    }


    @Test
    fun thatUIntRoundTripWorks() {
        TEST_BIGINTEGERS_POSITIVE_ONLY.forEach {
            assertThat(UIntETHType.ofNativeKotlinType(it, BitsTypeParams(256)).toKotlinType()).isEqualTo(it)
        }
    }

    @Test
    fun thatIntRoundTripWorks() {
        TEST_BIGINTEGERS_INCL_NEGATIVE.forEach {
            assertThat(IntETHType.ofNativeKotlinType(it, BitsTypeParams(256)).toKotlinType()).isEqualTo(it)
        }
    }

    @Test
    fun thatAddressRoundTripWorks() {
        TEST_ADDRESSES.forEach {
            assertThat(AddressETHType.ofNativeKotlinType(it).toKotlinType()).isEqualTo(it)
        }
    }

    @Test
    fun testBoolWorks() {
        assertThat(BoolETHType.ofNativeKotlinType(true).toKotlinType()).isEqualTo(true)
        assertThat(BoolETHType.ofNativeKotlinType(false).toKotlinType()).isEqualTo(false)
    }

    @Test
    fun testDecodeString() {
        val input = PaginatedByteArray(HexString("0x000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000046c69676900000000000000000000000000000000000000000000000000000000"))
        assertThat(StringETHType.ofPaginatedByteArray(input)?.toKotlinType()).isEqualTo("ligi")

    }

    @Test
    fun testDecodeInt() {
        val input0 = PaginatedByteArray(HexString("0x0000000000000000000000000000000000000000000000000000000000000000"))
        assertThat(IntETHType.ofPaginatedByteArray(input0, BitsTypeParams(8))?.toKotlinType()).isEqualTo(ZERO)

        val input1 = PaginatedByteArray(HexString("0x0000000000000000000000000000000000000000000000000000000000000001"))
        assertThat(IntETHType.ofPaginatedByteArray(input1, BitsTypeParams(8))?.toKotlinType()).isEqualTo(ONE)

        val input_1 = PaginatedByteArray(HexString("0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"))
        assertThat(IntETHType.ofPaginatedByteArray(input_1, BitsTypeParams(8))?.toKotlinType()).isEqualTo(BigInteger.valueOf(-1))

        val input127 = PaginatedByteArray(HexString("0x000000000000000000000000000000000000000000000000000000000000007f"))
        assertThat(IntETHType.ofPaginatedByteArray(input127, BitsTypeParams(8))?.toKotlinType()).isEqualTo(BigInteger.valueOf(127))

        val input_128 = PaginatedByteArray(HexString(("0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80")))
        assertThat(IntETHType.ofPaginatedByteArray(input_128, BitsTypeParams(16))?.toKotlinType()).isEqualTo(BigInteger.valueOf(-128))
    }
}