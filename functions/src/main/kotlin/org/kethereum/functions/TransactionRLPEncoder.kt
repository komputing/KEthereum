package org.kethereum.functions

import org.kethereum.functions.rlp.RLPList
import org.kethereum.functions.rlp.encode
import org.kethereum.functions.rlp.toRLP
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import org.walleth.khex.hexToByteArray

fun Transaction.toRLPList(signature: SignatureData?) = RLPList(listOf(
        nonce!!.toRLP(),
        gasPrice.toRLP(),
        gasLimit.toRLP(),
        (to?.hex?.let { it } ?: "0x").hexToByteArray().toRLP(),
        value.toRLP(),
        input.toByteArray().toRLP()
).let {

    if (signature == null) {
        it
    } else {
        it.plus(listOf(
                signature.v.toRLP(),
                signature.r.toRLP(),
                signature.s.toRLP()
        ))
    }
})

fun Transaction.encodeRLP(signature: SignatureData? = null) = toRLPList(signature).encode()