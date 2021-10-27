package org.kethereum.metadata

import io.ipfs.multiformats.multihash.Multihash
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import org.kethereum.metadata.model.*
import org.kethereum.model.ByteCode
import org.komputing.khex.extensions.toNoPrefixHexString
import java.math.BigInteger


@OptIn(ExperimentalSerializationApi::class)
fun ByteCode.getMetaDataCBOR(): MetaDataInByteCodeCBOR {
    val length = BigInteger(byteCode.sliceArray((byteCode.size - 2) until byteCode.size))
    val cbor = byteCode.sliceArray((byteCode.size - 2 - length.toInt()) until (byteCode.size - 2))
    return Cbor.decodeFromByteArray(MetaDataInByteCodeCBOR.serializer(), cbor)
}

fun ByteCode.getMetaDataIPFSCID(): String? = getMetaDataCBOR().ipfs?.let {
    Multihash.fromHexString(it.toNoPrefixHexString()).toBase58String()
}
