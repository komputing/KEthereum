package org.kethereum.metadata

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kethereum.abi.findByHexMethodSignature
import org.kethereum.abi.findByTextMethodSignature
import org.kethereum.abi.getAllFunctions
import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.getETHTypeInstanceOrNull
import org.kethereum.erc681.ERC681
import org.kethereum.metadata.model.*
import org.kethereum.metadata.repo.model.MetaDataNotAvailable
import org.kethereum.metadata.repo.model.MetaDataRepo
import org.kethereum.metadata.repo.model.MetaDataResolveFail
import org.kethereum.metadata.repo.model.MetaDataResolveResultOK
import org.kethereum.methodsignatures.getHexSignature
import org.kethereum.methodsignatures.model.HexMethodSignature
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toTextMethodSignature
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.Transaction

suspend fun TextMethodSignature.resolveFunctionUserDoc(
        contractAddress: Address,
        chain: ChainId,
        functionParamValuesList: List<String>,
        metaDataRepo: MetaDataRepo
): UserDocResult = withContext(Dispatchers.IO) {

    return@withContext when (val res = metaDataRepo.getMetaDataForAddressOnChain(contractAddress, chain)) {
        is MetaDataNotAvailable -> UserDocResultContractNotFound
        is MetaDataResolveResultOK -> {
            val metaData = res.data
            val methods = metaData.output.userdoc.methods

            val function = metaData.output.abi.getAllFunctions().findByTextMethodSignature(this@resolveFunctionUserDoc)

            methods[normalizedSignature]?.let {
                if (it is Map<*, *> && it["notice"] != null) {
                    var notice = it["notice"].toString()

                    function?.inputs?.forEachIndexed { index, input ->
                        notice = notice.replace("`${input.name}`", functionParamValuesList[index])
                    }
                    ResolvedUserDocResult(notice)

                } else null
            } ?: NoMatchingUserDocFound
        }
        is MetaDataResolveFail -> ResolveErrorUserDocResult(res.reason)
    }

}

suspend fun HexMethodSignature.resolveFunctionUserDoc(
        contractAddress: Address,
        chain: ChainId,
        data: ByteArray,
        metaDataRepo: MetaDataRepo
): UserDocResult = withContext(Dispatchers.IO) {

    when (val metaDataResult = metaDataRepo.getMetaDataForAddressOnChain(contractAddress, chain)) {
        is MetaDataNotAvailable -> UserDocResultContractNotFound
        is MetaDataResolveFail -> ResolveErrorUserDocResult(metaDataResult.reason)
        is MetaDataResolveResultOK -> {
            val metaData = metaDataResult.data
            val res = metaData.output.userdoc.methods

            val function = metaData.output.abi.getAllFunctions().findByHexMethodSignature(this@resolveFunctionUserDoc)

            return@withContext res[function!!.toTextMethodSignature().normalizedSignature]?.let {
                if (it is Map<*, *> && it["notice"] != null) {
                    var notice = it["notice"].toString()
                    val paginatedByteArray = PaginatedByteArray(data.slice(4 until data.size).toByteArray())
                    val valuesMap = function.inputs.map { input ->
                        function.name to getETHTypeInstanceOrNull(input.type, paginatedByteArray)?.toKotlinType()?.toString()
                    }

                    function.inputs.forEachIndexed { index, input ->
                        notice = notice.replace("`${input.name}`", valuesMap[index].second ?: "null")
                    }

                    ResolvedUserDocResult(notice)

                } else null
            } ?: NoMatchingUserDocFound

        }

    }
}

suspend fun Transaction.resolveFunctionUserDoc(metaDataRepo: MetaDataRepo) =
        getHexSignature().resolveFunctionUserDoc(to!!, ChainId(chain!!), input, metaDataRepo)

suspend fun ERC681.resolveFunctionUserDoc(
        chain: ChainId,
        metaDataRepo: MetaDataRepo
): UserDocResult = withContext(Dispatchers.IO) {
    val mutableAddress = address ?: return@withContext ResolveErrorUserDocResult("ERC-681 must have an address to resolve the userdoc")
    val signature = TextMethodSignature(function + "(" + "" + functionParams.joinToString(",") { it.first } + ")")
    return@withContext signature.resolveFunctionUserDoc(Address(mutableAddress), chain, functionParams.map { it.second }, metaDataRepo)
}