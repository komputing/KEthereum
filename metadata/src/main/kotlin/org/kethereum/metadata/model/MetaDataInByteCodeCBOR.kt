package org.kethereum.metadata.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString

@Serializable
class MetaDataInByteCodeCBOR(
    @ByteString
    val ipfs: ByteArray? = null,
    @ByteString
    val bzzr: ByteArray? = null,
    @ByteString
    val solc: ByteArray
)
