package org.kethereum.metadata

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import org.kethereum.metadata.model.EthereumMetaData
import org.kethereum.metadata.model.EthereumMetadataString

fun EthereumMetadataString.parse(moshi: Moshi = Moshi.Builder().build()): EthereumMetaData? {
    return try {
        val adapter = moshi.adapter<EthereumMetaData>(EthereumMetaData::class.java)
        adapter.fromJson(json)
    } catch (e: JsonEncodingException) {
        null
    }
}
