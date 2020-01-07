package org.kethereum.metadata

import com.squareup.moshi.Moshi
import org.kethereum.metadata.model.EthereumMetaData
import org.kethereum.metadata.model.EthereumMetadataString

fun EthereumMetadataString.parse(moshi: Moshi = Moshi.Builder().build()) : EthereumMetaData? {
    val adapter = moshi.adapter<EthereumMetaData>(EthereumMetaData::class.java)
    return adapter.fromJson(json)
}
