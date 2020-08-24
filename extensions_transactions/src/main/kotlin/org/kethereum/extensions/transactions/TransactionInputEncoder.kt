package org.kethereum.extensions.transactions

import org.kethereum.contract.abi.types.encodeTypes
import org.kethereum.contract.abi.types.getETHTypeInstance
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.model.EthereumFunctionCall
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

fun EthereumFunctionCall.toTransactionInput(): ByteArray {
    val parameterSignature = functionParams.joinToString(",") { it.first }
    val functionSignature = TextMethodSignature("$function($parameterSignature)")

    val encoded = encodeTypes(*(functionParams.map { pair ->
        getETHTypeInstance(pair.first, pair.second)
    }).toTypedArray())
    return HexString(functionSignature.toHexSignature().hex).hexToByteArray() + encoded
}
