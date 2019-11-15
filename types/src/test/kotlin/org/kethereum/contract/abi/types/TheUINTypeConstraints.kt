package org.kethereum.contract.abi.types

import org.junit.jupiter.api.Test
import org.kethereum.contract.abi.types.model.types.UIntETHType
import java.math.BigInteger.ONE
import java.math.BigInteger.valueOf
import kotlin.test.assertFailsWith

class TheUINTypeConstraints {

    @Test
    fun commonUINTBitLengthWorks() {
        UIntETHType.ofNativeKotlinType(ONE, "8")
        UIntETHType.ofNativeKotlinType(ONE, "24")
        UIntETHType.ofNativeKotlinType(ONE, "32")
        UIntETHType.ofNativeKotlinType(ONE, "64")
        UIntETHType.ofNativeKotlinType(ONE, "256")
    }

    @Test
    fun invalidUINTBitLengthsFail() {

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, "7")
        }

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, "264")
        }

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, "-1")
        }

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(ONE, "yolo")
        }
    }


    @Test
    fun whenItDoesNotFitItFails() {

        assertFailsWith(IllegalArgumentException::class) {
            UIntETHType.ofNativeKotlinType(valueOf(256), "8")
        }
    }


}