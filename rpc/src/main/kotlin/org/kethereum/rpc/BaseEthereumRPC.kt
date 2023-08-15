package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.kethereum.extensions.BigIntegerAdapter
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toHexString
import org.kethereum.model.*
import org.kethereum.rpc.model.*
import org.kethereum.rpc.model.BaseResponse
import org.kethereum.rpc.model.BlockInformationResponse
import org.kethereum.rpc.model.FeeHistoryResponse
import org.kethereum.rpc.model.StringResultResponse
import org.kethereum.rpc.model.TransactionResponse
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
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

    private val stringArrayAdapter: JsonAdapter<StringArrayResponse> = moshi.adapter(StringArrayResponse::class.java)

    override fun stringCall(function: String, params: String): StringResultResponse? {
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
    override fun accounts(): List<Address>? {
        val call = transport.call("eth_accounts", "")?.let { string ->
            stringArrayAdapter.fromJson(string)
        }
       return call?.result?.map { Address(it) }
    }

    @Throws(EthereumRPCException::class)
    override fun sign(address: Address, message: ByteArray): SignatureData? =
        stringCall("eth_sign", "\"${address.hex}\",\"${message.toHexString()}\"")
            ?.throwOrString()
            ?.let { SignatureData.fromHex(it) }
            ?.let { SignatureData(r = it.r, s = it.s, v = it.v.plus(27.toBigInteger())) }

    @Throws(EthereumRPCException::class)
    override fun getStorageAt(address: Address, position: String, block: String) =
        stringCall("eth_getStorageAt", "\"${address.hex}\",\"$position\",\"$block\"")?.throwOrString()?.let { HexString(it) }

    @Throws(EthereumRPCException::class)
    override fun getTransactionCount(address: Address, block: String) =
        stringCall("eth_getTransactionCount", "\"${address.hex}\",\"$block\"")?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun getCode(address: Address, block: String) = stringCall("eth_getCode", "\"${address.hex}\",\"$block\"")?.throwOrString()?.let {
        ByteCode(HexString(it).hexToByteArray())
    }

    @Throws(EthereumRPCException::class)
    override fun estimateGas(transaction: Transaction) = stringCall("eth_estimateGas", transaction.toJSON())?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun getBalance(address: Address, block: String) = stringCall("eth_getBalance", "\"${address.hex}\",\"$block\"")?.getBigIntegerFromStringResult()

    @Throws(EthereumRPCException::class)
    override fun getFeeHistory(blocks: Int, lastBlock: String, percentiles: String) =
        transport.call("eth_feeHistory", "\"0x${blocks.toString(16)}\",\"$lastBlock\",[$percentiles]")?.let { string ->
            feeHistoryAdapter.fromJsonNoThrow(string)
        }?.throwOnError()?.result

    @Throws(EthereumRPCException::class)
    override fun getTransactionByHash(hash: String) = transport.call("eth_getTransactionByHash", "\"$hash\"")?.let { string ->
        transactionAdapter.fromJsonNoThrow(string)
    }?.result?.toKethereumTransaction()
}

@Throws(EthereumRPCException::class)
private fun StringResultResponse.throwOrString() = throwOnError().result!!

@Throws(EthereumRPCException::class)
private fun <T : BaseResponse> T.throwOnError() = error?.let { throw EthereumRPCException(it.message, it.code) } ?: this

@Throws(EthereumRPCException::class)
private fun StringResultResponse.getBigIntegerFromStringResult() = HexString(throwOrString()).hexToBigInteger()
