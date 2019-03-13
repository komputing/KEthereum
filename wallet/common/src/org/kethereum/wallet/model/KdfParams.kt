package org.kethereum.wallet.model

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl

sealed class KdfParams {
    abstract var dklen: Int
    abstract var salt: String?

    companion object: KSerializer<KdfParams> {

        override val descriptor = object: SerialClassDescImpl("KDFParams") {
            init {
                addElement("dklen")
                addElement("salt")
                addElement("prf")
                addElement("c")
                addElement("n")
                addElement("p")
                addElement("r")
            }
        }

        @UseExperimental(ImplicitReflectionSerializer::class)
        override fun deserialize(decoder: Decoder): KdfParams {
            val inp: CompositeDecoder = decoder.beginStructure(descriptor)
            lateinit var dklen: String
            var salt: String? = null
            var prf: String? = null
            var c: String? = null
            var n: String? = null
            var p: String? = null
            var r: String? = null
            loop@ while (true) {
                when (val i = inp.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> dklen = inp.decodeStringElement(descriptor, i)
                    1 -> salt = inp.decodeStringElement(descriptor, i)
                    2 -> prf = inp.decodeStringElement(descriptor, i)
                    3 -> c = inp.decodeStringElement(descriptor, i)
                    4 -> n = inp.decodeStringElement(descriptor, i)
                    5 -> p = inp.decodeStringElement(descriptor, i)
                    6 -> r = inp.decodeStringElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
            inp.endStructure(descriptor)

            return when {
                prf != null && salt != null && c != null -> Aes128CtrKdfParams(
                    c = c.toInt(),
                    prf = prf,
                    dklen = dklen.toInt(),
                    salt = salt
                )
                n != null && p != null && r != null -> ScryptKdfParams (
                    n = n.toInt(),
                    p = p.toInt(),
                    r = r.toInt(),
                    dklen = dklen.toInt(),
                    salt = salt
                )
                else -> throw IllegalArgumentException("Could not detect KDFParams")
            }
        }

        @UseExperimental(ImplicitReflectionSerializer::class)
        override fun serialize(encoder: Encoder, obj: KdfParams) {
            return encoder.encode(obj)
        }
    }
}

@Serializable
data class Aes128CtrKdfParams(
    var c: Int = 0,
    var prf: String? = null,
    override var dklen: Int = 0,
    override var salt: String? = null
) : KdfParams()

@Serializable
data class ScryptKdfParams(
    var n: Int = 0,
    var p: Int = 0,
    var r: Int = 0,

    override var dklen: Int = 0,
    override var salt: String? = null
) : KdfParams()
