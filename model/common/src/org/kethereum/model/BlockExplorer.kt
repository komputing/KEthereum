package org.kethereum.model

interface BlockExplorer {
    fun getAddressURL(address: Address): String
    fun getTransactionURL(transactionHash: String): String
    fun getBlockURL(blockNum: Long): String
}