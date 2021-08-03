package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toHexString
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BigIntegerAdapter
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.FeeHistoryResponse
import org.kethereum.rpc.model.StringResultResponse
import org.kethereum.rpc.model.TransactionResponse
import org.komputing.khex.model.HexString
import java.io.IOException
import java.math.BigInteger

class EthereumRPCException(override val message: String, val code: Int) : IOException(message)

open class BaseEthereumRPC(private val transport: RPCTransport) : EthereumRPC {

    private val moshi = Moshi.Builder().build().newBuilder().add(BigIntegerAdapter()).build()

    private val stringResultAdapter: JsonAdapter<StringResultResponse> = moshi.adapter(StringResultResponse::class.java)

    private val blockInfoAdapter: JsonAdapter<BlockInformationResponse> = moshi.adapter(BlockInformationResponse::class.java)

    private val transactionAdapter: JsonAdapter<TransactionResponse> = moshi.adapter(TransactionResponse::class.java)

    private val feeHistoryAdapter: JsonAdapter<FeeHistoryResponse> = moshi.adapter(FeeHistoryResponse::class.java)

    private fun stringCall(function: String, params: String = ""): StringResultResponse? {
        return transport.call(function, params)?.let { string -> stringResultAdapter.fromJsonNoThrow(string) }
    }

    override fun getBlockByNumber(number: BigInteger) = transport.call("eth_getBlockByNumber", "\"${number.toHexString()}\", true")?.let { string ->
        blockInfoAdapter.fromJsonNoThrow(string)
    }?.result?.toBlockInformation()

    @Throws(EthereumRPCException::class)
    override fun sendRawTransaction(data: String) = stringCall("eth_sendRawTransaction", "\"$data\"")?.throwOrString()

    @Throws(EthereumRPCException::class)
    override fun blockNumber() = stringCall("eth_blockNumber")?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun call(transaction: Transaction, block: String) =
        stringCall("eth_call", "${transaction.toJSON()},\"$block\"")?.throwOrString()?.let { HexString(it) }

    @Throws(EthereumRPCException::class)
    override fun gasPrice() = stringCall("eth_gasPrice")?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun clientVersion() = stringCall("web3_clientVersion")?.throwOrString()

    @Throws(EthereumRPCException::class)
    override fun chainId() = stringCall("eth_chainId")?.getBigIntegerFromStringResult()?.let { ChainId(it) }

    @Throws(EthereumRPCException::class)
    override fun getStorageAt(address: String, position: String, block: String) =
        stringCall("eth_getStorageAt", "\"$address\",\"$position\",\"$block\"")?.throwOrString()?.let { HexString(it) }

    @Throws(EthereumRPCException::class)
    override fun getTransactionCount(address: String, block: String) =
        stringCall("eth_getTransactionCount", "\"$address\",\"$block\"")?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun getCode(address: String, block: String) = stringCall("eth_getCode", "\"$address\",\"$block\"")?.throwOrString()?.let { HexString(it) }

    @Throws(EthereumRPCException::class)
    override fun estimateGas(transaction: Transaction) = stringCall("eth_estimateGas", transaction.toJSON())?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun getBalance(address: Address, block: String) = stringCall("eth_getBalance", "\"${address.hex}\",\"$block\"")?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun getFeeHistory(blocks: Int, lastBlock: String) = transport.call("eth_feeHistory", "\"0x${blocks.toString(16)}\",\"$lastBlock\",[]")?.let { string ->
        feeHistoryAdapter.fromJsonNoThrow(string)
    }?.result

    @Throws(EthereumRPCException::class)
    override fun getTransactionByHash(hash: String) = transport.call("eth_getTransactionByHash", "\"$hash\"")?.let { string ->
        transactionAdapter.fromJsonNoThrow(string)
    }?.result?.toKethereumTransaction()
}

@Throws(EthereumRPCException::class)
private fun StringResultResponse.throwOrString() = if (error != null)
    throw (EthereumRPCException(error.message, error.code))
else
    result

@Throws(EthereumRPCException::class)
private fun StringResultResponse.getBigIntegerFromStringResult() = HexString(throwOrString()).hexToBigInteger()
