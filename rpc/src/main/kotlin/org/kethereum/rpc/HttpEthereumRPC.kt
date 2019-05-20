package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BigIntegerAdapter
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.StringResultResponse
import org.kethereum.rpc.model.TransactionResponse
import java.io.IOException
import java.security.GeneralSecurityException


val JSONMediaType: MediaType = MediaType.parse("application/json")!!

class HttpEthereumRPC(val baseURL: String, private val okhttp: OkHttpClient = OkHttpClient().newBuilder().build()) : EthereumRPC {

    private val moshi = Moshi.Builder().build().newBuilder().add(BigIntegerAdapter()).build()

    private val stringResultAdapter: JsonAdapter<StringResultResponse> = moshi.adapter(StringResultResponse::class.java)

    private val blockInfoAdapter: JsonAdapter<BlockInformationResponse> = moshi.adapter(BlockInformationResponse::class.java)

    private val transactionAdapter: JsonAdapter<TransactionResponse> = moshi.adapter(TransactionResponse::class.java)


    private fun buildRequest(body: RequestBody) = Request.Builder().url(baseURL)
            .method("POST", body)
            .build()

    private fun buildRequest(medhod: String, params: String = "") = buildRequest(RequestBody.create(JSONMediaType, """{"jsonrpc":"2.0","method":"$medhod","params":[$params],"id":1}"""))

    private fun stringCall(function: String, params: String = ""): StringResultResponse? {
        return executeCallToString(function, params)?.let { string -> stringResultAdapter.fromJsonNoThrow(string) }
    }

    private fun executeCallToString(function: String, params: String) = try {
        okhttp.newCall(buildRequest(function, params)).execute().body().use { body ->
            body?.string()
        }
    } catch (e: IOException) {
        null
    } catch (e: GeneralSecurityException) {
        null
    }

    override fun getBlockByNumber(number: String) = executeCallToString("eth_getBlockByNumber", "\"$number\", true")?.let { string ->
        blockInfoAdapter.fromJsonNoThrow(string)
    }?.result?.toBlockInformation()

    override fun sendRawTransaction(data: String) = stringCall("eth_sendRawTransaction", "\"$data\"")

    override fun blockNumber() = stringCall("eth_blockNumber")

    override fun call(transaction: Transaction, block: String) = stringCall("eth_call", "${transaction.toJSON()},\"$block\"")

    override fun gasPrice() = stringCall("eth_gasPrice")

    override fun clientVersion() = stringCall("web3_clientVersion")

    override fun chainId() = stringCall("eth_chainId")

    override fun getStorageAt(address: String, position: String, block: String ) = stringCall("eth_getStorageAt", "\"$address\",\"$position\",\"$block\"")

    override fun getTransactionCount(address: String, block: String) = stringCall("eth_getTransactionCount", "\"$address\",\"$block\"")

    override fun getCode(address: String, block: String) = stringCall("eth_getCode", "\"$address\",\"$block\"")

    override fun estimateGas(transaction: Transaction) = stringCall("eth_estimateGas", transaction.toJSON())

    override fun getBalance(address: Address, block: String) = stringCall("eth_getBalance", "\"${address.hex}\",\"$block\"")

    override fun getTransactionByHash(hash: String) = executeCallToString("eth_getTransactionByHash", "\"$hash\"")?.let { string ->
        transactionAdapter.fromJsonNoThrow(string)
    }?.result?.toKethereumTransaction()



}


