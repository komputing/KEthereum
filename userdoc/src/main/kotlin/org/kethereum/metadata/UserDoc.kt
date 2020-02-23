package org.kethereum.metadata

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kethereum.abi.findByTextMethodSignature
import org.kethereum.abi.getAllFunctions
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.erc681.ERC681
import org.kethereum.metadata.model.*
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.model.Address
import org.kethereum.model.ChainId


private val DEFAULT_METADATA_REPO_URLS = listOf(
        "https://verification.komputing.org/repository/contract/byChainId/"
)

suspend fun TextMethodSignature.resolveFunctionUserDoc(
        contractAddress: Address,
        chain: ChainId,
        functionParamValuesList: List<String>,
        okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
        baseURL: List<String> = DEFAULT_METADATA_REPO_URLS
): UserDocResult = withContext(Dispatchers.IO) {
    val url = baseURL.first() + chain.value + "/" + contractAddress.withERC55Checksum() + "/metadata.json"
    val response = okHttpClient.newCall(Request.Builder().url(url).build()).execute()

    when (response.code()) {
        404 -> {
            return@withContext UserDocResultContractNotFound
        }

        200 -> {
            val metaData = response.body()?.use {
                val result = it.string()
                EthereumMetadataString(result)
            }?.parse()
            val res = metaData?.output?.userdoc?.methods

            val function = metaData?.output?.abi?.getAllFunctions()?.findByTextMethodSignature(this@resolveFunctionUserDoc)

            return@withContext res?.get(normalizedSignature)?.let {
                if (it is Map<*, *> && it["notice"] != null) {
                    var notice = it["notice"].toString()

                    function?.inputs?.forEachIndexed { index, input ->
                        notice = notice.replace("`${input.name}`", functionParamValuesList[index])
                    }
                    ResolvedUserDocResult(notice)

                } else null
            } ?: NoMatchingUserDocFound
        }
        else -> {
            return@withContext ResolveErrorUserDocResult("Error: " + response.code() + " " + response.message())
        }
    }
}

suspend fun ERC681.resolveFunctionUserDoc(
        chain: ChainId,
        okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
        baseURL: List<String> = DEFAULT_METADATA_REPO_URLS
): UserDocResult = withContext(Dispatchers.IO) {
    val mutableAddress = address ?: return@withContext ResolveErrorUserDocResult("ERC-681 must have an address to resolve the userdoc")
    val signature = TextMethodSignature(function + "(" + "" + functionParams.joinToString(",") { it.first } + ")")
    return@withContext signature.resolveFunctionUserDoc(Address(mutableAddress), chain, functionParams.map { it.second }, okHttpClient, baseURL)
}