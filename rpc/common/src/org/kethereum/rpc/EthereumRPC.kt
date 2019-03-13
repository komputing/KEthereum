package org.kethereum.rpc

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import kotlinx.io.errors.IOException
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import org.kethereum.model.Address
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.response.StringResultResponse

class EthereumRPC(val baseURL: String) {

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    private val json = Json.nonstrict

    private fun buildRequest(body: String): HttpRequestBuilder {
        return HttpRequestBuilder().apply {
            url(baseURL)
            this.method = HttpMethod.Post
            this.body = body
        }
    }

    private fun buildRequest(method: String, params: String = ""): HttpRequestBuilder {
        return buildRequest("""{"jsonrpc":"2.0","method":"$method","params":[$params],"id":1}""")
    }

    private suspend fun executeCallToString(function: String, params: String): String? = try {
        client.request<String>(buildRequest(function, params))
    } catch (e: IOException) {
        null
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    private suspend fun stringCall(function: String, params: String = ""): StringResultResponse? {
        return executeCallToString(function, params)?.let { response -> json.parse(response) }
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    suspend fun getBlockByNumber(number: String): BlockInformation? {
        return executeCallToString("eth_getBlockByNumber", "\"$number\", true")
            ?.let { string -> json.parse<BlockInformationResponse>(string) }
            ?.result
            ?.toBlockInformation()
    }

    suspend fun sendRawTransaction(data: String) =
        stringCall("eth_sendRawTransaction", "\"$data\"")

    suspend fun blockNumber() =
        stringCall("eth_blockNumber")

    suspend fun call(callObject: String, block: String) =
        stringCall("eth_call", "$callObject,\"$block\"")

    suspend fun gasPrice() =
        stringCall("eth_gasPrice")

    suspend fun clientVersion() =
        stringCall("web3_clientVersion")

    suspend fun getStorageAt(address: String, position: String, block: String) =
        stringCall("eth_getStorageAt", "\"$address\",\"$position\",\"$block\"")

    suspend fun getTransactionCount(address: String, block: String) =
        stringCall("eth_getTransactionCount", "\"$address\",\"$block\"")

    suspend fun getCode(address: String, block: String) =
        stringCall("eth_getCode", "\"$address\",\"$block\"")

    suspend fun estimateGas(callObject: String, block: String) =
        stringCall("eth_estimateGas", "$callObject,\"$block\"")

    suspend fun getBalance(address: Address, block: String) =
        stringCall("eth_getBalance", "\"${address.hex}\",\"$block\"")
}


