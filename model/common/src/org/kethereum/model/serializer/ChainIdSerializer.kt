package org.kethereum.model.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.kethereum.model.ChainId

class ChainIdSerializer: KSerializer<ChainId> {

    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("ChainId")

    override fun serialize(encoder: Encoder, obj: ChainId) {
        encoder.encodeLong(obj.value)
    }

    override fun deserialize(decoder: Decoder): ChainId {
        return ChainId(decoder.decodeLong())
    }
}