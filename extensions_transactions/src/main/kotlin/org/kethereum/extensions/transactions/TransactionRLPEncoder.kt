package org.kethereum.extensions.transactions

import org.kethereum.eip1559.detector.isEIP1559
import org.kethereum.model.SignatureData
import org.kethereum.model.SignedTransaction
import org.kethereum.model.Transaction
import org.kethereum.rlp.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.math.BigInteger

fun Transaction.toRLPList(signature: SignatureData?) = RLPList(
    (if (isEIP1559()) {
        listOf(
            chain!!.toRLP(),
            nonce!!.toRLP(),
            maxPriorityFeePerGas!!.toRLP(),
            maxFeePerGas!!.toRLP(),
            gasLimit!!.toRLP(),
            HexString(to?.hex ?: "0x").hexToByteArray().toRLP(),
            value!!.toRLP(),
            input.toRLP(),
            RLPList(emptyList())
        ).maybeAddSignature(signature, isEIP1559 = true)
    } else
        listOf(
            nonce!!.toRLP(),
            gasPrice!!.toRLP(),
            gasLimit!!.toRLP(),
            HexString(to?.hex ?: "0x").hexToByteArray().toRLP(),
            value!!.toRLP(),
            input.toRLP()
        ).maybeAddSignature(signature))
)

fun List<RLPType>.maybeAddSignature(signature: SignatureData?, isEIP1559: Boolean = false) = if (signature == null) this
else plus(
    listOf(
        (if (isEIP1559) (signature.v - BigInteger("27")) else signature.v).toRLP(),
        signature.r.toRLP(),
        signature.s.toRLP()
    )
)

fun Transaction.encodeAsEIP1559Tx(signature: SignatureData? = null) = ByteArray(1) { 2 } + toRLPList(signature).encode()
fun Transaction.encodeLegacyTxRLP(signature: SignatureData? = null) = toRLPList(signature).encode()

fun Transaction.encode(signature: SignatureData? = null) = if (isEIP1559()) encodeAsEIP1559Tx(signature) else encodeLegacyTxRLP(signature)

fun SignedTransaction.encodeLegacyTxRLP() = transaction.toRLPList(signatureData).encode()
fun SignedTransaction.encode() = transaction.encode(signatureData)