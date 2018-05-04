package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.kethereum.rpc.model.BigIntegerAdapter
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.BlockNumberResponse


val JSONMediaType: MediaType = MediaType.parse("application/json")!!

class EthereumRPC(val baseURL: String, val okhttp: OkHttpClient = OkHttpClient().newBuilder().build()) {

    val moshi = Moshi.Builder().build().newBuilder().add(BigIntegerAdapter()).build()

    val blockNumberAdapter: JsonAdapter<BlockNumberResponse> = moshi.adapter(BlockNumberResponse::class.java)

    val blockInfoAdapter: JsonAdapter<BlockInformationResponse> = moshi.adapter(BlockInformationResponse::class.java)

    private fun buildBlockRequest() = buildRequest(RequestBody.create(JSONMediaType, "{\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1}"))

    private fun buildBlockByNumberRequest(number: String)
            = buildRequest(RequestBody.create(JSONMediaType, "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockByNumber\",\"params\":[\"$number\", true],\"id\":1}}"))

    private fun buildRequest(body: RequestBody) = Request.Builder().url(baseURL)
            .method("POST", body)
            .build()!!

    fun getBlockNumberString() = okhttp.newCall(buildBlockRequest()).execute().body().use { body ->
        body?.source().let { blockNumberAdapter.fromJson(it) }
    }?.result

    fun getBlockByNumber(number: String) = okhttp.newCall(buildBlockByNumberRequest(number)).execute().body().use { body ->
        body?.source().let { blockInfoAdapter.fromJson(it) }
    }?.result

}