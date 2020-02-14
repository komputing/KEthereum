package org.walleth.kethereum.etherscan

import org.kethereum.model.Address
import org.kethereum.model.BlockExplorer

class EtherScanBlockExplorer(private val prefix: String) : BlockExplorer {

    private val baseURL by lazy { "https://" + (if (prefix.isEmpty()) prefix else "$prefix.") + "etherscan.io" }

    override fun getAddressURL(address: Address) = "$baseURL/address/${address.hex}"
    override fun getTransactionURL(transactionHash: String) = "$baseURL/tx/$transactionHash"
    override fun getBlockURL(blockNum: Long) = "$baseURL/block/$blockNum"

}