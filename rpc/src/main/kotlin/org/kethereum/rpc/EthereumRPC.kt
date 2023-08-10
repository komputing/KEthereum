package org.kethereum.rpc

import org.kethereum.model.*
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.FeeHistory
import org.kethereum.rpc.model.StringResultResponse
import org.komputing.khex.model.HexString
import java.math.BigInteger


interface EthereumRPC {

    fun getBlockByNumber(number: BigInteger): BlockInformation?
    fun getTransactionByHash(hash: String): SignedTransaction?
    fun stringCall(function: String, params: String = ""): StringResultResponse?
    fun sendRawTransaction(data: String): String?
    fun blockNumber(): BigInteger?
    fun call(transaction: Transaction, block: String = "latest"): HexString?
    fun gasPrice(): BigInteger?
    fun clientVersion(): String?
    fun chainId(): ChainId?
    fun accounts(): List<Address>?
    fun sign(address: Address, message: ByteArray): SignatureData?
    fun getStorageAt(address: Address, position: String, block: String = "latest"): HexString?
    fun getTransactionCount(address: Address, block: String = "latest"): BigInteger?
    fun getCode(address: Address, block: String = "latest"): ByteCode?
    fun estimateGas(transaction: Transaction): BigInteger?
    fun getBalance(address: Address, block: String = "latest"): BigInteger?
    fun getFeeHistory(blocks: Int, lastBlock: String = "latest", percentiles: String = ""): FeeHistory?
}


