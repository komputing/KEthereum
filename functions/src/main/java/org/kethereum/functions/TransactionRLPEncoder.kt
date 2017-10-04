package org.kethereum.functions

import org.kethereum.functions.rlp.RLPList
import org.kethereum.functions.rlp.encode
import org.kethereum.functions.rlp.toRLP
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import org.walleth.khex.hexToByteArray

fun Transaction.encodeRLP(withSignature: SignatureData? = null) = RLPList(listOf(
        nonce!!.toRLP(),
        gasPrice.toRLP(),
        gasLimit.toRLP(),
        (to?.hex?.let { it } ?: "0x").hexToByteArray().toRLP(),
        value.toRLP(),
        input.toByteArray().toRLP()
).let {
    if (withSignature == null) {
        it
    } else {
        it.plus(listOf(
                withSignature.v.toRLP(),
                withSignature.r.toRLP(),
                withSignature.s.toRLP()
        ))
    }
}).encode()