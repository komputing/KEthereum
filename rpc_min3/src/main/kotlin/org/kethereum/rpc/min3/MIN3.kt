package org.kethereum.rpc.min3

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.kethereum.rpc.BaseEthereumRPC
import org.kethereum.rpc.JSONMediaType
import org.kethereum.rpc.RPCTransport
import org.kethereum.rpc.min3.model.IN3NodeListResponse

val MAINNET_BOOTNODES = listOf(
        "https://in3-node-1.keil-connect.com",
        "https://in3-node-from.space",
        "https://in3-v2.slock.it/mainnet/nd-1"
)

val GOERLI_BOOTNODES = listOf(
        "https://in3-v2.slock.it/goerli/nd-1",
        "https://in3-v2.slock.it/goerli/nd-2"
)

internal val in3nodeListResponseAdapter = Moshi.Builder().build().adapter<IN3NodeListResponse>(IN3NodeListResponse::class.java)

class MIN3RPC(bootNodes: List<String> = MAINNET_BOOTNODES) : BaseEthereumRPC(MIN3Transport(bootNodes))

class MIN3Transport(private val bootNodes: List<String>,
                    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
) : RPCTransport {

    private val nodes = mutableSetOf<String>()

    init {
        nodes.addAll(bootNodes)
    }

    override fun call(payload: String): String? {
        maybeUpdateNodeList()

        repeat(3) {
            val maybeResult = try {
                okHttpClient.newCall(buildRequest(payload)).execute().body()?.string()
            } catch (e: Exception) {
                null
            }
            if (maybeResult != null) {
                return maybeResult
            }
        }
        return null // no result after 3 attempts
    }

    private fun maybeUpdateNodeList() {
        if (bootNodes.size == nodes.size) {
            val nodeListRequest = buildRequest("""{"jsonrpc":"2.0","method":"in3_nodeList","params":[],"id":1}""")
            val newNodeURLs = okHttpClient.newCall(nodeListRequest).execute().body()?.string()?.let { json ->
                in3nodeListResponseAdapter.fromJson(json)?.result?.nodes
            }?.map { it.url }

            if (newNodeURLs != null) {
                nodes.addAll(newNodeURLs)
            }
        }
    }

    private fun buildRequest(body: RequestBody) = Request.Builder().url(nodes.random())
            .method("POST", body)
            .build()

    private fun buildRequest(payload: String) = buildRequest(RequestBody.create(JSONMediaType, payload))

}
