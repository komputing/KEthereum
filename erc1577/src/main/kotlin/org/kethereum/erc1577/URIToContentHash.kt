package org.kethereum.erc1577

import io.ipfs.multiformats.multihash.Multihash
import okio.Buffer
import org.kethereum.erc1577.model.ContentHash
import org.kethereum.erc1577.model.InvalidSchemeToURIError
import org.kethereum.erc1577.model.InvalidSwarmURL
import org.kethereum.erc1577.model.SuccessfulToContentHashResult
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import org.komputing.kvarint.writeVarUInt
import java.lang.IllegalArgumentException
import java.net.URI

fun getResult(byteArray: ByteArray) = SuccessfulToContentHashResult(ContentHash(byteArray))
fun URI.toContentHash() = when (scheme) {
    "ipfs" -> {
        val buffer = Buffer()
        val hash: ByteArray = Multihash.decode(Multihash.fromBase58String(host).raw).digest

        buffer.writeVarUInt(0xe3U) // storageByte
        buffer.writeVarUInt(0x1U) // CID
        buffer.writeVarUInt(0x70U) // contentType
        buffer.writeVarUInt(18U) // hashFunction
        buffer.writeVarUInt(hash.size.toUInt())
        buffer.write(hash)
        getResult(buffer.readByteArray())
    }
    "ipns" -> {
        val buffer = Buffer()
        val hash: ByteArray = host.toByteArray()

        buffer.writeVarUInt(0xe5U) // storageByte
        buffer.writeVarUInt(0x1U) // CID
        buffer.writeVarUInt(0x70U) // contentType
        buffer.writeVarUInt(0x0U) // hashFunction
        buffer.writeVarUInt(hash.size.toUInt())
        buffer.write(hash)
        getResult(buffer.readByteArray())
    }
    "bzz" -> {
        val hash = try {
            HexString(host).hexToByteArray()
        } catch (iae: IllegalArgumentException) {
            null
        }
        if (hash == null) {
            InvalidSwarmURL(host)
        } else {
            val buffer = Buffer()
            buffer.writeVarUInt(0xe4U) // storageByte
            buffer.writeVarUInt(0x1U) // CID
            buffer.writeVarUInt(0xfaU) // contentType
            buffer.writeVarUInt(0x1BU) // hashFunction


            buffer.writeVarUInt(hash.size.toUInt()) // contentType
            buffer.write(hash) // storageByte

            getResult(buffer.readByteArray())
        }
    }
    else -> {
        InvalidSchemeToURIError(scheme)
    }
}