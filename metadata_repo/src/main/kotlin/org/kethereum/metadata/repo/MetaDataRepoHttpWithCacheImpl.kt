package org.kethereum.metadata.repo

import okhttp3.OkHttpClient
import okhttp3.Request
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.metadata.model.EthereumMetadataString
import org.kethereum.metadata.parse
import org.kethereum.metadata.repo.model.*
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import java.io.File

class MetaDataRepoHttpWithCacheImpl(private val repoURL: String = "https://repo.sourcify.dev/contract",
                                    private val cacheDir: File? = null,
                                    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder().build()
) : MetaDataRepo {
    override fun getMetaDataForAddressOnChain(address: Address, chain: ChainId): MetaDataResolveResult {
        if (cacheDir != null) {
            val cacheFile = File(cacheDir, address.toString() + chain.value)
            if (cacheFile.exists()) {
                val metaData = EthereumMetadataString(cacheFile.readText()).parse()
                if (metaData != null) {
                    return MetaDataResolveResultOK(metaData)
                }
            }

        }
        val url = repoURL + "/" + chain.value + "/" + address.withERC55Checksum() + "/metadata.json"
        return try {
            val response = okHttpClient.newCall(Request.Builder().url(url).build()).execute()
            when (response.code) {
                404 -> MetaDataNotAvailable
                200 -> {
                    val metaDataString = response.body?.use {
                        val result = it.string()
                        EthereumMetadataString(result)
                    }
                    val metaData = metaDataString?.parse()

                    if (metaData != null) {
                        if (cacheDir != null) {
                            File(cacheDir, address.toString() + chain.value).apply {
                                createNewFile()
                                writeText(metaDataString.json)
                            }
                        }
                        MetaDataResolveResultOK(metaData)
                    } else {
                        MetaDataResolveFail("Cannot parse metadata from $url")
                    }
                }
                else -> MetaDataResolveFail("Cannot get MetaData " + response.message + " ( code " + response.code + ")")
            }
        } catch (e: Exception) {
            MetaDataResolveFail("Cannot get MetaData " + e.message)
        }
    }

}