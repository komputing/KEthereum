package org.kethereum.contract.abi.types

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ONE
import org.junit.jupiter.api.Test
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.types.UIntETHType
import kotlin.test.assertFailsWith

class TheUINTypeConstraints {

    @Test
    fun commonUINTBitLengthWorks() {
        UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(8))
        UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(24))
        UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(32))
        UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(64))
        UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(256))
    }

    @Test
    fun invalidUINTBitLengthsFail() {

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(7))
        }

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(264))
        }

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, BitsTypeParams(-1))
        }

    }


    @Test
    fun whenItDoesNotFitItFails() {

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(BigInteger(256), BitsTypeParams(8))
        }
    }


}