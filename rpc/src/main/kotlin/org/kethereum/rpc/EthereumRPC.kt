package org.kethereum.rpc

import org.kethereum.model.Address
import org.kethereum.model.SignedTransaction
import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BlockInformation
import org.kethereum.rpc.model.StringResultResponse


interface EthereumRPC {

    fun getBlockByNumber(number: String): BlockInformation?
    fun getTransactionByHash(hash: String): SignedTransaction?

    fun sendRawTransaction(data: String): StringResultResponse?
    fun blockNumber(): StringResultResponse?
    fun call(transaction: Transaction, block: String): StringResultResponse?
    fun gasPrice(): StringResultResponse?
    fun clientVersion(): StringResultResponse?
    fun chainId(): StringResultResponse?
    fun getStorageAt(address: String, position: String, block: String = "latest"): StringResultResponse?
    fun getTransactionCount(address: String, block: String = "latest"): StringResultResponse?
    fun getCode(address: String, block: String): StringResultResponse?
    fun estimateGas(transaction: Transaction): StringResultResponse?
    fun getBalance(address: Address, block: String = "latest"): StringResultResponse?
}


