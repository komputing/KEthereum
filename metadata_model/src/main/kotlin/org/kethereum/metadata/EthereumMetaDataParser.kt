package org.kethereum.metadata

import com.squareup.moshi.*
import org.kethereum.extensions.BigIntegerAdapter
import org.kethereum.metadata.model.EthereumMetaData
import org.kethereum.metadata.model.EthereumMetadataString

fun EthereumMetadataString.parse(moshi: Moshi = Moshi.Builder().add(BigIntegerAdapter()).build()): EthereumMetaData? {
    return try {
        val adapter = moshi.adapter(EthereumMetaData::class.java)
        adapter.fromJson(json)
    } catch (e: JsonEncodingException) {
        null
    }
}
