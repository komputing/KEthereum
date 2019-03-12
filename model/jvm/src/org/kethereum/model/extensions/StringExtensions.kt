package org.kethereum.model.extensions

import org.kethereum.model.number.BigInteger

actual fun String.format(value: BigInteger): String {
    return String.format(this, value.value)
}

actual fun String.parseInt(radix: Int): Int {
    return Integer.parseInt(this, radix)
}