package org.kethereum.model.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.kethereum.model.number.BigInteger

@Serializer(forClass = BigInteger::class)
internal class BigIntegerSerializer: KSerializer<BigInteger> {

    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("BigInteger")

    override fun serialize(encoder: Encoder, obj: BigInteger) {
        encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): BigInteger {
        return BigInteger(decoder.decodeString())
    }
}