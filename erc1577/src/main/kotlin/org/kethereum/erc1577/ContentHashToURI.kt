@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package org.kethereum.erc1577

import io.ipfs.multiformats.multihash.Multihash
import io.ipfs.multiformats.multihash.Type
import okio.BufferedSource
import okio.buffer
import okio.source
import org.kethereum.erc1577.model.*
import org.komputing.kbase58.encodeToBase58String
import org.komputing.khex.extensions.toNoPrefixHexString
import org.komputing.kvarint.readVarUInt

fun ContentHash.toURI(): ToURIResult {
    if (byteArray.size < 6) {
        return ContentTooShort()
    }
    val buffer: BufferedSource = byteArray.inputStream().source().buffer()

    val storageByte = buffer.readVarUInt()

    val cid = buffer.readVarUInt()
    if (cid != 1U) {
        return InvalidCIDError(cid)
    }

    return when (storageByte) {
        0xe3U -> {
            val contentType = buffer.readVarUInt()
            val hashFunction: UInt = buffer.readVarUInt()
            val hashLength = buffer.readVarUInt().toInt()
            val content = buffer.readByteArray()

            when {
                hashLength != content.size -> HashLengthError(hashLength, content.size)
                contentType != 0x70U -> ContentTypeError(contentType, 0x70U)
                else -> {
                    SuccessfulToURIResult("ipfs://" + Multihash.encode(content, hashFunction).encodeToBase58String())
                }
            }
        }

        0xe4U -> {
            val contentType = buffer.readVarUInt()
            val hashFunction = buffer.readVarUInt()
            val hashLength = buffer.readVarUInt().toInt()
            val content = buffer.readByteArray()

            when {
                contentType != 0xfaU -> ContentTypeError(contentType, 0xfaU)
                hashLength != content.size -> HashLengthError(hashLength, content.size)

                else -> SuccessfulToURIResult("bzz://" + content.toNoPrefixHexString())
            }
        }
        else -> InvalidStorageSystem(storageByte)
    }
}
