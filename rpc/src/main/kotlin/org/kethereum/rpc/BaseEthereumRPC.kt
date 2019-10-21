package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BigIntegerAdapter
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.StringResultResponse
import org.kethereum.rpc.model.TransactionResponse

open class BaseEthereumRPC(private val transport: RPCTransport) : EthereumRPC {

    private val moshi = Moshi.Builder().build().newBuilder().add(BigIntegerAdapter()).build()

    private val stringResultAdapter: JsonAdapter<StringResultResponse> = moshi.adapter(StringResultResponse::class.java)

    private val blockInfoAdapter: JsonAdapter<BlockInformationResponse> = moshi.adapter(BlockInformationResponse::class.java)

    private val transactionAdapter: JsonAdapter<TransactionResponse> = moshi.adapter(TransactionResponse::class.java)

    private fun stringCall(function: String, params: String = ""): StringResultResponse? {
        return transport.call(function, params)?.let { string -> stringResultAdapter.fromJsonNoThrow(string) }
    }

    override fun getBlockByNumber(number: String) = transport.call("eth_getBlockByNumber", "\"$number\", true")?.let { string ->
        blockInfoAdapter.fromJsonNoThrow(string)
    }?.result?.toBlockInformation()

    override fun sendRawTransaction(data: String) = stringCall("eth_sendRawTransaction", "\"$data\"")

    override fun blockNumber() = stringCall("eth_blockNumber")

    override fun call(transaction: Transaction, block: String) = stringCall("eth_call", "${transaction.toJSON()},\"$block\"")

    override fun gasPrice() = stringCall("eth_gasPrice")

    override fun clientVersion() = stringCall("web3_clientVersion")

    override fun chainId() = stringCall("eth_chainId")

    override fun getStorageAt(address: String, position: String, block: String) = stringCall("eth_getStorageAt", "\"$address\",\"$position\",\"$block\"")

    override fun getTransactionCount(address: String, block: String) = stringCall("eth_getTransactionCount", "\"$address\",\"$block\"")

    override fun getCode(address: String, block: String) = stringCall("eth_getCode", "\"$address\",\"$block\"")

    override fun estimateGas(transaction: Transaction) = stringCall("eth_estimateGas", transaction.toJSON())

    override fun getBalance(address: Address, block: String) = stringCall("eth_getBalance", "\"${address.hex}\",\"$block\"")

    override fun getTransactionByHash(hash: String) = transport.call("eth_getTransactionByHash", "\"$hash\"")?.let { string ->
        transactionAdapter.fromJsonNoThrow(string)
    }?.result?.toKethereumTransaction()
}