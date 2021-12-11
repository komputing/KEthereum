package org.kethereum.rpc

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.Exception
import java.security.GeneralSecurityException

val JSONMediaType: MediaType = "application/json".toMediaType()

class HttpEthereumRPC(val baseURL: String, okhttp: OkHttpClient = OkHttpClient().newBuilder().build())
    : BaseEthereumRPC(HttpTransport(baseURL, okhttp))

class HttpTransport(private val baseURL: String,
                    private val okhttp: OkHttpClient = OkHttpClient().newBuilder().build(),
                    private val debug: Boolean = false) : RPCTransport {

    override fun call(payload: String) = try {
        val result = okhttp.newCall(buildRequest(payload)).execute().body.use { body ->
            body?.string()
        }

        if (debug) {
            println("HttpTransport> via $baseURL payload: $payload")
            println("HttpTransport> got $result")

        }
        result
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

class MultiHostHttpTransport(urls: List<String>, private val okhttp: OkHttpClient = OkHttpClient().newBuilder().build()) : RPCTransport {

    private val successMap = urls.associateWith { 0 }.toMutableMap()

    override fun call(payload: String): String? {
        val url: String? = successMap.maxByOrNull { it.value }?.key
        try {
            val executedCall = okhttp.newCall(buildRequest(url!!, payload)).execute()
            val result = executedCall.body.use { body ->
                body?.string()
            }

            if (executedCall.code == 200) {
                successMap[url] = (successMap[url] ?: 0) + 1
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        successMap[url!!] = (successMap[url] ?: 0) - 1
        return null
    }


    private fun buildRequest(url: String, body: RequestBody) = Request.Builder().url(url)
            .method("POST", body)
            .build()

    private fun buildRequest(url: String, payload: String) = buildRequest(url, payload.toRequestBody(JSONMediaType))

}

