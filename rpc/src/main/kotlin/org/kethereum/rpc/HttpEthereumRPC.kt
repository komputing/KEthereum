package org.kethereum.rpc

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.security.GeneralSecurityException

val JSONMediaType: MediaType = MediaType.parse("application/json")!!


class HttpEthereumRPC(val baseURL: String, okhttp: OkHttpClient = OkHttpClient().newBuilder().build())
    : BaseEthereumRPC(HttpTransport(baseURL, okhttp))

class HttpTransport(private val baseURL: String, private val okhttp: OkHttpClient = OkHttpClient().newBuilder().build()) : RPCTransport {

    override fun call(payload: String) = try {
        okhttp.newCall(buildRequest(payload)).execute().body().use { body ->
            body?.string()
        }
    } catch (e: IOException) {
        null
    } catch (e: GeneralSecurityException) {
        null
    }


    private fun buildRequest(body: RequestBody) = Request.Builder().url(baseURL)
            .method("POST", body)
            .build()

    private fun buildRequest(payload: String) = buildRequest(RequestBody.create(JSONMediaType, payload))

}


