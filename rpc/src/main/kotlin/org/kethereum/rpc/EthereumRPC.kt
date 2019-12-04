package org.kethereum.rpc

import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.SignedTransaction
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BlockInformation
import java.math.BigInteger


interface EthereumRPC {

    fun getBlockByNumber(number: BigInteger): BlockInformation?
    fun getTransactionByHash(hash: String): SignedTransaction?

    fun sendRawTransaction(data: String): String?
    fun blockNumber(): BigInteger?
    fun call(transaction: Transaction, block: String = "latest"): ByteArray?
    fun gasPrice(): BigInteger?
    fun clientVersion(): String?
    fun chainId(): ChainId?
    fun getStorageAt(address: String, position: String, block: String = "latest"): ByteArray?
    fun getTransactionCount(address: String, block: String = "latest"): BigInteger?
    fun getCode(address: String, block: String): ByteArray?
    fun estimateGas(transaction: Transaction): BigInteger?
    fun getBalance(address: Address, block: String = "latest"): BigInteger?
}


