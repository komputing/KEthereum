package org.kethereum.metadata.repo.model

import org.kethereum.metadata.model.EthereumMetaData
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.Transaction

sealed class MetaDataResolveResult

data class MetaDataResolveResultOK(val data: EthereumMetaData) : MetaDataResolveResult()
data class MetaDataResolveFail(val reason: String) : MetaDataResolveResult()
object MetaDataNotAvailable : MetaDataResolveResult()

fun MetaDataRepo.getMetaDataForTransaction(tx: Transaction) = tx.to?.let { to ->
    tx.chain?.let { chain ->
        getMetaDataForAddressOnChain(to, ChainId(chain))
    }
}?: MetaDataResolveFail("Cannot resolve for " + tx.to + "@" + tx.chain)

interface MetaDataRepo {
    fun getMetaDataForAddressOnChain(address: Address, chain: ChainId): MetaDataResolveResult
}