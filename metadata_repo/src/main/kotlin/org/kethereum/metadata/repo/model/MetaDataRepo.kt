package org.kethereum.metadata.repo.model

import org.kethereum.metadata.model.EthereumMetaData
import org.kethereum.model.Address
import org.kethereum.model.ChainId

sealed class MetaDataResolveResult

data class MetaDataResolveResultOK(val data: EthereumMetaData) : MetaDataResolveResult()
data class MetaDataResolveFail(val reason: String) : MetaDataResolveResult()
object MetaDataNotAvailable : MetaDataResolveResult()

interface MetaDataRepo {
    fun getMetaDataForAddressOnChain(address: Address, chain: ChainId): MetaDataResolveResult
}