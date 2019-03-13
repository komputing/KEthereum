package org.kethereum.model

import kotlinx.serialization.Serializable
import org.kethereum.model.number.BigInteger
import org.kethereum.model.number.BigInteger.Companion.ZERO
import org.kethereum.model.serializer.BigIntegerSerializer

@Serializable
data class SignatureData(
    @Serializable(with = BigIntegerSerializer::class) var r: BigInteger = ZERO,
    @Serializable(with = BigIntegerSerializer::class) var s: BigInteger = ZERO,
    var v: Byte = 0
)
