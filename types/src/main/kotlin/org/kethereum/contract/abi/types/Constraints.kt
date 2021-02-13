package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.BYTE_IN_BITS

val BYTES_COUNT_CONSTRAINT: (Int) -> Unit = {
    require(it >= 1) { "bytes count MUST be more than 0" }
    require(it <= 32) { "bytes count MUST be less than 32" }
}

val INT_BITS_CONSTRAINT: (Int) -> Unit = {
    require(it % BYTE_IN_BITS <= 0) { "bits%$BYTE_IN_BITS MUST be 0 but is not for $it" }

    require(it >= 8) { "cannot have less than 8 bit - but got $it" }

    require(it <= 256) { "cannot have more than 256 bits - but got $it" }
}