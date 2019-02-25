package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.kethereum.model.Address
import org.kethereum.rpc.model.BigIntegerAdapter
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.StringResultResponse


val JSONMediaType: MediaType = MediaType.parse("application/json")!!

class EthereumRPC(val baseURL: String, private val okhttp: OkHttpClient = OkHttpClient().newBuilder().build()) {

    private val moshi = Moshi.Builder().build().newBuilder().add(BigIntegerAdapter()).build()

    private val stringResultAdapter: JsonAdapter<StringResultResponse> = moshi.adapter(StringResultResponse::class.java)

    private val blockInfoAdapter: JsonAdapter<BlockInformationResponse> = moshi.adapter(BlockInformationResponse::class.java)


    private fun buildRequest(body: RequestBody) = Request.Builder().url(baseURL)
            .method("POST", body)
            .build()

    private fun buildRequest(medhod: String, params: String = "") = buildRequest(RequestBody.create(JSONMediaType, """{"jsonrpc":"2.0","method":"$medhod","params":[$params],"id":1}"""))

    fun getBlockNumberString() = okhttp.newCall(buildRequest("eth_blockNumber")).execute().body().use { body ->
        body?.source()?.use { stringResultAdapter.fromJson(it) }
    }?.result

    fun getBlockByNumber(number: String) = okhttp.newCall(buildRequest("eth_blockByNumber", "\"$number \", true")).execute().body().use { body ->
        body?.source()?.use { blockInfoAdapter.fromJson(it) }
    }?.result?.toBlockInformation()


    fun sendRawTransaction(data: String) = okhttp.newCall(buildRequest("eth_sendRawTransaction", "\"$data\"")).execute().body().use { body ->
        body?.source()?.use { stringResultAdapter.fromJson(it) }
    }


    fun getBalance(address: Address, block: String) = okhttp.newCall(buildRequest("eth_getBalance", "\"${address.hex}\",\"$block\"")).execute().body().use { body ->
        body?.source()?.use { stringResultAdapter.fromJson(it) }
    }
}


