package org.kethereum.erc681

import org.kethereum.contract.abi.types.encodeTypes
import org.kethereum.contract.abi.types.getETHTypeInstance
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import org.walleth.khex.hexToByteArray


fun ERC681.toTransactionInput(): ByteArray {
    val parameterSignature = functionParams.joinToString(",") { it.first }
    val functionSignature = TextMethodSignature("$function($parameterSignature)")

    val encoded = encodeTypes(*(functionParams.map { pair ->
        getETHTypeInstance(pair.first, pair.second)!!
    }).toTypedArray())
    return (functionSignature.toHexSignature().hex).hexToByteArray() + encoded
}
