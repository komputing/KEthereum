package org.kethereum.functions

import org.kethereum.functions.rlp.RLPList
import org.kethereum.functions.rlp.encode
import org.kethereum.functions.rlp.toRLP
import org.kethereum.model.Transaction
import java.math.BigInteger

fun Transaction.encodeRLP() = RLPList(listOf(
        BigInteger.valueOf(nonce!!).toRLP(),
        gasPrice.toRLP(),
        gasLimit.toRLP(),
        (to?.hex?.let { it } ?: "0x").hexToByteArray().toRLP(),
        value.toRLP(),
        input.toByteArray().toRLP()
).let {
    val _signatureData = this.signatureData
    if (_signatureData == null) {
        it
    } else {
        it.plus(listOf(
                _signatureData.v.toRLP(),
                _signatureData.r.toRLP(),
                _signatureData.s.toRLP()
        ))
    }
}).encode()