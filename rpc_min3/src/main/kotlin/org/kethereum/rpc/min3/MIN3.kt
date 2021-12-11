package org.kethereum.rpc.min3

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.kethereum.model.ChainId
import org.kethereum.rpc.BaseEthereumRPC
import org.kethereum.rpc.JSONMediaType
import org.kethereum.rpc.RPCTransport
import org.kethereum.rpc.min3.model.IN3NodeListResponse

val MAINNET_BOOTNODES = listOf(
        "https://in3-v2.slock.it/mainnet/nd-1",
        "https://in3-v2.slock.it/mainnet/nd-2",
        "https://in3-v2.slock.it/mainnet/nd-3"
)

val GOERLI_BOOTNODES = listOf(
        "https://in3-v2.slock.it/goerli/nd-1",
        "https://in3-v2.slock.it/goerli/nd-2"
)

fun getMin3BootnNdesByChainId(chainId: ChainId) = when (chainId.value.toLong()) {
    1L -> MAINNET_BOOTNODES
    5L -> GOERLI_BOOTNODES
    else -> null
}

internal val in3nodeListResponseAdapter = Moshi.Builder().build().adapter<IN3NodeListResponse>(IN3NodeListResponse::class.java)

fun getMin3RPC(bootNodes: List<String> = MAINNET_BOOTNODES,
               okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
) = BaseEthereumRPC(MIN3Transport(bootNodes, okHttpClient))

fun getMin3RPC(chainId: ChainId,
               okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
) = getMin3BootnNdesByChainId(chainId)?.let { nodes ->
    getMin3RPC(nodes, okHttpClient)
}

class MIN3Transport(private val bootNodes: List<String>,
                    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
                    private val max_retries: Int = 42,
                    private val debug: Boolean = false
) : RPCTransport {

    private val nodes = mutableMapOf<String, Int>()

    init {
        nodes.putAll(bootNodes.associateWith { 0 })
    }

    override fun call(payload: String): String? {
        maybeUpdateNodeList()
        return requestWithRepeat(payload)
    }

    private fun requestWithRepeat(payload: String): String? {

        repeat(max_retries) { retry ->
            val url = nodes.maxByOrNull { it.value }?.key!!
            if (debug) {
                println("MIN3Transport> request payload $payload - retry $retry")
                println("MIN3Transport> trying to fetch from $url")
            }
            val request = buildRequest(payload, url)
            val maybeResult = try {
                val execute = okHttpClient.newCall(request).execute()
                val responseString = execute.body?.use { it.string() }

                if (debug) {
                    println("MIN3Transport> response code: ${execute.code} - body: $responseString")
                }

                if (execute.code == 200 && responseString?.startsWith("{") == true) {
                    responseString
                } else {
                    null
                }
            } catch (e: Exception) {
                if (debug) {
                    println("MIN3Transport> exception code: ${e.message}")
                    e.printStackTrace()
                }
                null
            }
            if (maybeResult != null) {
                nodes[url] = (nodes[url] ?: 0) + 1
                return maybeResult
            }
            nodes[url] = (nodes[url] ?: 0) - 1
        }
        return null // no result after 5 attempts
    }

    private fun maybeUpdateNodeList() {
        if (bootNodes.size == nodes.size) {
            val newNodeURLs = requestWithRepeat("""{"jsonrpc":"2.0","method":"in3_nodeList","params":[],"id":1}""")?.let { json ->
                in3nodeListResponseAdapter.fromJson(json)?.result?.nodes
            }?.map { it.url }?.filter { !nodes.containsKey(it) }

            if (newNodeURLs != null) {
                nodes.putAll(newNodeURLs.associateWith { 0 })
            }
        }
    }

    private fun buildRequest(body: RequestBody, url: String) = Request.Builder().url(url)
            .method("POST", body)
            .build()

    private fun buildRequest(payload: String, url: String) = buildRequest(payload.toRequestBody(JSONMediaType), url)
}